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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /** 单次拉取批次大小 */
    private static final long PULL_BATCH_SIZE = 300L;

    /**
     * 本地已处理的偏移量
     * 约定：processedOffset = 已处理的最后一个 offset，下次读取从 processedOffset + 1 开始
     */
    private final static Map<String, AtomicLong> LOCAL_PROCESSED_OFFSET_MAP = new ConcurrentHashMap<>();


    /**
     * 增量拉取流程（offset 驱动）
     * 约定：processedOffset = 已处理的最后一个 offset，下次读取从 processedOffset + 1 开始
     *
     * @param datasetName datasetName
     */
    public void pullIncrementalByOffset(String datasetName) {

        // ========== 前置校验：本地偏移量必须已初始化 ==========
        AtomicLong processedOffsetRef = LOCAL_PROCESSED_OFFSET_MAP.get(datasetName);
        if (processedOffsetRef == null) {
            log.warn("Offset not initialized for dataset: {}. Call pullIncrementalByLowerBound first.", datasetName);
            return;
        }
        IncrementalExecutionConfig executionConfig = snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();

        while (true) {
            long processedOffset = processedOffsetRef.get();

            // ========== 第一步：检查是否追上最新 offset ==========
            // 读取 Redis 中的最新 offset，如果本地已追上则结束本轮拉取
            String latestKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
            String latestOffsetStr = redisTemplate.opsForValue().get(latestKey);
            if (latestOffsetStr == null) {
                log.debug("[pullIncr] No latest offset for dataset={}, sleeping...", datasetName);
                break;
            }
            long latestOffset = Long.parseLong(latestOffsetStr);
            if (processedOffset >= latestOffset) {
                log.debug("[pullIncr] Dataset={} caught up. localOffset={}, latestOffset={}, sleeping...",
                        datasetName, processedOffset, latestOffset);
                break;
            }

            // ========== 第二步：计算分片信息 & 从 Redis ZSET 拉取一批数据 ==========
            // nextReadOffset：本次读取的起始 offset（processedOffset + 1，因为 processedOffset 是"已处理最后一个"）
            long nextReadOffset = processedOffset + 1;

            // shardId：nextReadOffset 所属的分片编号（offset 从1开始，所以先-1再做整除）
            long shardId = (nextReadOffset - 1) / maxMembersSize;
            String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY.format(datasetName, shardId);

            // batchEndOffset：本批次读取的上界 offset（闭区间，不超过 latestOffset）
            long batchEndOffset = Math.min(nextReadOffset + PULL_BATCH_SIZE - 1, latestOffset);
            Set<ZSetOperations.TypedTuple<String>> tuples =
                    redisTemplate.opsForZSet().rangeByScoreWithScores(shardKey, (double) nextReadOffset, (double) batchEndOffset);

            // ========== 第三步：拉取为空时的跳转策略 ==========
            // 当前分片无数据时，直接跳到下一个分片起始位置继续拉取
            // 注意：不能直接跳到 latestOffset，因为中间分片可能还有未消费的数据
            // 如果下一个分片起始位置已超过 latestOffset，说明中间确实没有更多数据了，直接 break
            if (CollectionUtils.isEmpty(tuples)) {
                // nextShardFirstOffset：下一个分片的第一个 offset（shardId+1 分片的起始位置）
                long nextShardFirstOffset = (shardId + 1) * maxMembersSize + 1;

                if (nextShardFirstOffset > latestOffset) {
                    // 下一分片起始位置已超过最新 offset，说明已追上，直接结束
                    processedOffsetRef.set(latestOffset);
                    log.debug("[pullIncr] Empty shard & next shard beyond latest. dataset={}, shardId={}, offset {} -> {}",
                            datasetName, shardId, processedOffset, latestOffset);
                    break;
                }

                // 跳到下一个分片起始位置继续拉取
                // 置为 nextShardFirstOffset - 1，保持 processedOffset = "已处理最后一个" 的约定
                long jumpToProcessedOffset = nextShardFirstOffset - 1;
                processedOffsetRef.set(jumpToProcessedOffset);
                log.debug("[pullIncr] Empty shard, jump to next shard. dataset={}, shardId={}, offset {} -> {}",
                        datasetName, shardId, processedOffset, jumpToProcessedOffset);
                continue;
            }

            // ========== 第四步：遍历本批次数据（业务处理由调用方自行扩展） ==========
            for (ZSetOperations.TypedTuple<String> data : tuples) {
                Double score = data.getScore();
                String value = data.getValue();
                log.trace("[pullIncr] Record detail: dataset={}, offset={}, value={}", datasetName, score, value);
            }

            // ========== 第五步：更新本地偏移量 ==========
            // 取本批次中最大的 score 作为新的 processedOffset
            long maxProcessedOffset = tuples.stream()
                    .mapToLong(t -> Objects.requireNonNull(t.getScore()).longValue())
                    .max()
                    .orElse(processedOffset);
            processedOffsetRef.set(maxProcessedOffset);

            log.info("[pullIncr] Pulled {} records. dataset={}, offset {} -> {}",
                    tuples.size(), datasetName, processedOffset, maxProcessedOffset);
        }
    }


    /**
     * 时间驱动的起始点定位
     * 首次调用：从快照元数据获取增量拉取时间下界，按配置的分钟周期逐个 GET 查找 offset
     * 续接调用：本地已有 processedOffset，直接走 offset 驱动模式
     *
     * 约定：processedOffset = 已处理的最后一个 offset。
     * foundOffset 是时间窗口内的最新 offset，对应数据已在全量快照中，因此直接作为 processedOffset，
     * 后续 pullIncrementalByOffset 会从 processedOffset + 1 开始读取增量数据。
     */
    public void pullIncrementalByLowerBound(String datasetName) {

        // ========== 第一步：已有 offset，直接走 offset 驱动 ==========
        AtomicLong existingOffset = LOCAL_PROCESSED_OFFSET_MAP.get(datasetName);
        if (existingOffset != null) {
            log.info("[pullIncr] Resuming from local offset={} for dataset={}", existingOffset.get(), datasetName);
            pullIncrementalByOffset(datasetName);
            return;
        }

        // ========== 第二步：首次加载 — 从快照元数据获取增量拉取时间下界 ==========
        IncrementalExecutionConfig executionConfig = snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        int intervalMinutes = executionConfig.getIncrementalConfig().getTimeIntervalMinutes();
        DateTimeFormatter minuteFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

        // 2.1 获取最新快照版本
        String latestKey = SnapshotPathEnum.REDIS_SNAPSHOT_LATEST_VERSION_KEY.format(datasetName);
        String latestVersion = redisTemplate.opsForValue().get(latestKey);
        if (latestVersion == null) {
            throw new IllegalStateException("No full snapshot found in Redis");
        }

        // 2.2 读取快照元数据，提取增量拉取时间下界
        String metaKey = SnapshotPathEnum.REDIS_SNAPSHOT_VERSION_META_KEY.format(datasetName, latestVersion);
        String metaJson = redisTemplate.opsForValue().get(metaKey);
        SnapshotMetadata metadata = new Gson().fromJson(metaJson, SnapshotMetadata.class);
        String searchStartCycleKey = metadata.getIncrPullTimeLowerBound();
        log.info("[pullIncr] Snapshot metadata loaded. dataset={}, version={}, cycleKey={}",
                datasetName, latestVersion, searchStartCycleKey);

        // ========== 第三步：按周期逐个查找 offset ==========
        // 从时间下界开始，按 intervalMinutes 粒度逐个周期查找 Redis 中的 timeOffset 记录
        // 直到找到有效 offset 或追上当前周期为止
        LocalDateTime searchTime = LocalDateTime.parse(searchStartCycleKey, minuteFormatter);
        long foundOffset = -1;
        int cycleCount = 0;

        while (true) {
            // 3.1 计算当前查找周期对应的 Redis key
            String cycleKey = executionConfig.getIncrIntervalMinuteKey(
                    searchTime.plusMinutes((long) cycleCount * intervalMinutes));
            String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, cycleKey);

            // 3.2 尝试获取该周期的 offset
            String offsetValue = redisTemplate.opsForValue().get(timeOffsetKey);
            if (offsetValue != null) {
                foundOffset = Long.parseLong(offsetValue);
                log.info("[pullIncr] Found offset={} at cycleKey={} for dataset={}", foundOffset, cycleKey, datasetName);
                break;
            }

            // 3.3 查不到数据且已到当前周期 → 说明已追上，停下来
            String nowCycleKey = executionConfig.getIncrIntervalMinuteKey(LocalDateTime.now());
            if (cycleKey.compareTo(nowCycleKey) >= 0) {
                log.info("[pullIncr] Reached current cycle={}, no more data for dataset={}", cycleKey, datasetName);
                break;
            }

            cycleCount++;
            log.debug("[pullIncr] No offset at cycleKey={} for dataset={}, trying next cycle", cycleKey, datasetName);
        }

        // ========== 第四步：初始化本地偏移量 ==========
        // foundOffset >= 0：时间窗口内找到有效 offset，直接作为 processedOffset
        // foundOffset == -1：未找到任何 offset，初始化为 0，等待下一轮周期写入
        long initOffset = foundOffset >= 0 ? foundOffset : 0;
        LOCAL_PROCESSED_OFFSET_MAP.put(datasetName, new AtomicLong(initOffset));
        log.info("[pullIncr] Local offset initialized. dataset={}, initOffset={}", datasetName, initOffset);

        // ========== 第五步：切换到 offset 驱动模式 ==========
        if (foundOffset >= 0) {
            pullIncrementalByOffset(datasetName);
        } else {
            log.info("[pullIncr] No offset found for dataset={}, will retry next cycle", datasetName);
        }
    }

}