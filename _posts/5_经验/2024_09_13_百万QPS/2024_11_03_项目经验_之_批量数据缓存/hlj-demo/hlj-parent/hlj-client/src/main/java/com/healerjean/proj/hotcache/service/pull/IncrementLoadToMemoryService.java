package com.healerjean.proj.hotcache.service.pull;

import com.google.gson.Gson;
import com.healerjean.proj.hotcache.config.DatasetIncrementalConfig;
import com.healerjean.proj.hotcache.config.IncrementalExecutionConfig;
import com.healerjean.proj.hotcache.config.SnapshotGlobalConfig;
import com.healerjean.proj.hotcache.enums.SnapshotPathEnum;
import com.healerjean.proj.hotcache.model.SnapshotMetadata;
import com.healerjean.proj.hotcache.service.cache.InMemoryUserTagCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 增量加载到内存
 *
 * @author zhangyujin
 * @date 2025/11/6
 */
@Service
@Slf4j
public class IncrementLoadToMemoryService {

    @Autowired
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private InMemoryUserTagCache userTagCache;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final long PULL_BATCH_SIZE = 300L;

    /**
     * 本地已处理的偏移量（必须先通过 pullIncrementalByLowerBound 初始化）
     */
    private final static Map<String, AtomicLong> LOCAL_PROCESSED_OFFSET_MAP = new ConcurrentHashMap<>();



    /**
     * 增量拉取流程
     *
     * @param datasetName datasetName
     */
    public void pullIncrementalByOffset(String datasetName) {
        if (!LOCAL_PROCESSED_OFFSET_MAP.containsKey(datasetName)) {
            log.warn("Offset not initialized for dataset: {}. Skipping pull. Call pullIncrementalByLowerBound first.", datasetName);
            return;
        }
        IncrementalExecutionConfig executionConfig = snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        AtomicLong atomicCurrentOffset = LOCAL_PROCESSED_OFFSET_MAP.get(datasetName);

        while (true) {
            // 1、计算当前应读取的 shardId，
            long currentOffset = atomicCurrentOffset.get();
            String latestKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
            String latestOffsetStr = redisTemplate.opsForValue().get(latestKey);
            long latestOffset = Long.parseLong(latestOffsetStr);
            if (currentOffset >= latestOffset) {
                // 当前 shard 已追上最新 offset → 结束
                log.debug("Dataset {} caught up to latest offset {}, sleeping...", datasetName, latestOffset);
                break;
            }

            // 2、从 Redis ZSET 拉取一批数据
            long shardId = currentOffset / maxMembersSize;
            String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY.format(datasetName, shardId);
            long nextOffset = currentOffset + 1;
            double maxScore = Math.min(nextOffset + PULL_BATCH_SIZE - 1, latestOffset);
            Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet().rangeByScoreWithScores(shardKey, (double) nextOffset, maxScore);

            // 3、防止拉取数据为空，且当前 offset 未更新，则继续拉取
            if (CollectionUtils.isEmpty(tuples)) {
                // 当前 shard 无数据，尝试跳转到下一个 shard 起始位置
                long nextShardStart = (shardId + 1) * maxMembersSize;
                long jumpTo = Math.min(nextShardStart, latestOffset);
                // 如果跳转幅度很大（比如超过一个 batch），先小步试探
                if (jumpTo - currentOffset > PULL_BATCH_SIZE * 2) {
                    // 只前进一小步，避免跨度过大跳过异常数据
                    jumpTo = currentOffset + 1;
                }

                if (jumpTo > currentOffset) {
                    atomicCurrentOffset.set(jumpTo);
                    log.debug("Advanced offset to {} for dataset {}", jumpTo, datasetName);
                    continue;
                }
                // 已到最新，但仍无数据 → 可能数据延迟，稍后重试
                log.debug("No data found in shard {} for dataset {}, but not caught up yet.", shardId, datasetName);
                break;
            }

            // 4、批量处理 & 更新偏移量
            for (ZSetOperations.TypedTuple<String> data : tuples) {
                Double score = data.getScore();
                String value = data.getValue();
            }

            // 5、更新本地 offset 为本次拉取的最大 score
            // 更新 offset 为本次最大 score（注意：不再 +1）
            long maxProcessedOffset = tuples.stream()
                    .mapToLong(t -> Objects.requireNonNull(t.getScore()).longValue())
                    .max()
                    .orElse(currentOffset);

            atomicCurrentOffset.set(maxProcessedOffset);
            log.debug("Pulled {} records for dataset {}, offset updated to {}",
                    tuples.size(), datasetName, maxProcessedOffset);
        }
    }


    /**
     * 时间驱动的起始点定位
     */
    public void pullIncrementalByLowerBound(String datasetName) {
        // 1、从 Redis 获取最新快照版本（latestVersion）
        String latestKey = SnapshotPathEnum.REDIS_SNAPSHOT_LATEST_VERSION_KEY.format(datasetName);
        String latestVersion = redisTemplate.opsForValue().get(latestKey);
        if (latestVersion == null) {
            throw new IllegalStateException("No full snapshot found in Redis");
        }

        // 2. 解析其元数据 SnapshotMetadata，获取 incrPullTimeLowerBound（时间戳）
        String metaKey = SnapshotPathEnum.REDIS_SNAPSHOT_VERSION_META_KEY.format(datasetName, latestVersion);
        String metaJson = redisTemplate.opsForValue().get(metaKey);
        SnapshotMetadata metadata = new Gson().fromJson(metaJson, SnapshotMetadata.class);
        long incrPullTimeLowerBound = metadata.getIncrPullTimeLowerBound();
        String timePeriodKey = SnapshotPathEnum.REDIS_INCR_TIME_PERIOD_KEY.format(datasetName);

        // 3、 查找 >= 时间下界的第一个时间点
        Set<String> candidates = redisTemplate.opsForZSet().rangeByScore(timePeriodKey, (double) incrPullTimeLowerBound, Double.MAX_VALUE, 0, 1);
        if (CollectionUtils.isEmpty(candidates)) {
            LOCAL_PROCESSED_OFFSET_MAP.put(datasetName, new AtomicLong(0));
            log.info("No time period found >= {} for dataset {}", incrPullTimeLowerBound, datasetName);
            return;
        }

        // 4、通过 REDIS_INCR_TIME_OFFSET_KEY 反查对应的 offset
        String firstTimeStr = candidates.iterator().next();
        String offsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, firstTimeStr);
        String offsetValue = redisTemplate.opsForValue().get(offsetKey);
        long latestOffset = Long.parseLong(offsetValue);

        // 5、初始化 LOCAL_PROCESSED_OFFSET_MAP 并启动 pullIncrementalByOffset
        LOCAL_PROCESSED_OFFSET_MAP.put(datasetName, new AtomicLong(latestOffset));
        pullIncrementalByOffset(datasetName);
    }

}