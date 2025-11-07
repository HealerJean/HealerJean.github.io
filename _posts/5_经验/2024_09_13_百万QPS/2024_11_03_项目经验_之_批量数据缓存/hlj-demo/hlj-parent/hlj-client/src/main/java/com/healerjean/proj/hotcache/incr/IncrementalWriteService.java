package com.healerjean.proj.hotcache.incr;


import com.healerjean.proj.hotcache.config.DatasetIncrementalConfig;
import com.healerjean.proj.hotcache.config.IncrementalExecutionConfig;
import com.healerjean.proj.hotcache.config.SnapshotGlobalConfig;
import com.healerjean.proj.hotcache.enums.SnapshotPathEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 消息队列消费者 - 处理增量数据写入
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Slf4j
@Component
public class IncrementalWriteService {


    /***
     * snapshotGlobalConfig
     */
    @Resource
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 增量任务写入
     *
     * @param datasetName datasetName
     */
    public void write(String data, String datasetName) {
        IncrementalExecutionConfig executionConfig = snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Long ttlSeconds = incrementalConfig.getTtlSeconds();
        LocalDateTime dateTime = LocalDateTime.now();
        Duration ttl = Duration.ofSeconds(ttlSeconds);

        // 1. 获取全局唯一 offset
        String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
        Long offset = redisTemplate.opsForValue().increment(offsetKey);
        // 2. 如果 Redis 返回 null（如 key 不存在且 incr 失败），从 DB 恢复
        if (offset == null || offset <= 0) {
            offset = restoreOffsetFromDb(datasetName, offsetKey);
        }else {
            // 6. todo 将 offset 写入数据库
        }

        // 2. 根据 offset 计算分片 ID（每 MAX_MEMBERS_PER_ZSET 一个分片）
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        long shardId = (offset - 1) / maxMembersSize;
        String zsetKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY.format(datasetName, shardId);

        // 3. 写入 ZSET（score = offset）
        Boolean added = redisTemplate.opsForZSet().add(zsetKey, data, (double) offset);
        if (Boolean.TRUE.equals(added)) {
            redisTemplate.expire(zsetKey, ttl);
        }

        // 4. 写入时间索引
        long incrTimeSecond = executionConfig.getIncrIntervalTimeSecond(dateTime, 1);
        String timePeriodKey = SnapshotPathEnum.REDIS_INCR_TIME_PERIOD_KEY.format(datasetName);
        String timeOffset = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, String.valueOf(incrTimeSecond));
        redisTemplate.opsForValue().set(timeOffset, offset.toString(), ttl);
        redisTemplate.opsForZSet().add(timePeriodKey, String.valueOf(incrTimeSecond), (double) incrTimeSecond);
        Long currentSize = redisTemplate.opsForZSet().size(timePeriodKey);
        if (currentSize != null && currentSize > maxMembersSize) {
            long removeCount = currentSize - maxMembersSize;
            redisTemplate.opsForZSet().removeRange(timePeriodKey, 0, removeCount - 1);
        }
        redisTemplate.expire(timePeriodKey, ttl);

        // 6. todo 将 offset 写入数据库

    }

    /**
     * 从数据库恢复
     *
     * @param datasetName datasetName
     * @param offsetKey   offsetKey
     * @return {@link Long}
     */
    private long restoreOffsetFromDb(String datasetName, String offsetKey) {
        String lockKey = "lock:offset:init:" + datasetName;
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", Duration.ofSeconds(10));
        if (Boolean.FALSE.equals(locked)) {
            throw new RuntimeException("Interrupted while waiting for offset init");
        }
        try {
            // 从 DB 读取最新 offset
            // Long dbOffset = offsetRepository.getCurrentOffsetFromDb(datasetName);
            // long nextOffset = dbOffset + 1;
            // redisTemplate.opsForValue().set(offsetKey, String.valueOf(nextOffset));
            // return nextOffset;
        } finally {
            redisTemplate.delete(lockKey);
        }
        return 1;
    }


}