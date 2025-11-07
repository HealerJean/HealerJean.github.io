package com.healerjean.proj.hotcache.incr;


import com.healerjean.proj.hotcache.config.DatasetIncrementalConfig;
import com.healerjean.proj.hotcache.config.IncrementalExecutionConfig;
import com.healerjean.proj.hotcache.config.SnapshotGlobalConfig;
import com.healerjean.proj.hotcache.enums.SnapshotPathEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 消息队列消费者 - 处理增量数据写入
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Slf4j
@Component
public class IncrementalLuaWriteService {


    private final static RedisScript<String> WRITE_INCREMENTAL_SCRIPT = RedisScript.of(
            "redis.call('ZADD', KEYS[1], ARGV[2], ARGV[1]);" +
                    "redis.call('EXPIRE', KEYS[1], ARGV[3]);" +
                    "redis.call('SET', KEYS[2], ARGV[2], 'EX', ARGV[3]);" +
                    "redis.call('ZADD', KEYS[3], ARGV[4], ARGV[4]);" +
                    "redis.call('EXPIRE', KEYS[3], ARGV[3]);" +
                    "local maxMembers = tonumber(ARGV[5]);" +
                    "local currentSize = redis.call('ZCARD', KEYS[3]);" +
                    "if currentSize > maxMembers then " +
                    "  local removeCount = currentSize - maxMembers; " +
                    "  redis.call('ZREMRANGEBYRANK', KEYS[3], 0, removeCount - 1); " +
                    "end; " +
                    "return 1;",
            Long.class
    );

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
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        LocalDateTime now = LocalDateTime.now();

        // 1. 获取全局唯一 offset
        String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
        Long offset = redisTemplate.opsForValue().increment(offsetKey);
        if (offset == null || offset <= 0) {
            offset = restoreOffsetFromDb(datasetName, offsetKey);
            // 注意：restore 后建议 SET offsetKey offset+1 避免重复，此处简化
        }else {
            // 6. todo 将 offset 写入数据库
        }

        // 2. 计算 shardId 和所有 keys
        long shardId = (offset - 1) / maxMembersSize;
        String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY.format(datasetName, shardId);
        long incrTimeSecond = executionConfig.getIncrIntervalTimeSecond(now, 1);
        String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, String.valueOf(incrTimeSecond));
        String timePeriodKey = SnapshotPathEnum.REDIS_INCR_TIME_PERIOD_KEY.format(datasetName);

        // 3. 准备 Lua 参数
        List<String> keys = Arrays.asList(shardKey, timeOffsetKey, timePeriodKey);
        List<String> args = Arrays.asList(
                data,
                String.valueOf(offset),
                String.valueOf(ttlSeconds),
                String.valueOf(incrTimeSecond),
                String.valueOf(maxMembersSize)
        );

        // 4. 执行 Lua 脚本
        redisTemplate.execute(WRITE_INCREMENTAL_SCRIPT, keys, args);
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