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
 * 消息队列消费者 - 处理增量数据写入（Lua 原子方案）
 * <p>
 * 将 INCR + ZADD 合并为一个 Lua 脚本，保证 offset 分配与 ZSET 写入的原子性，
 * 避免并发场景下 offset 已分配但数据未写入导致读取端丢数据的问题。
 * <p>
 * timeOffsetKey 不参与原子操作，单独写入：
 * - 它只用于首次定位起始 offset，晚写1秒不影响正确性
 * - 拆出后 Lua 脚本只需操作 offsetKey + shardKey，降低 Hash Tag 约束范围
 * </p>
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Slf4j
@Component
public class IncrementalLuaWriteService {

    /**
     * Lua 原子写入脚本（INCR + ZADD）
     * <p>
     * KEYS[1] = offsetKey       — incr:{dataset}:latestOffset
     * KEYS[2] = shardPrefix     — incr:{dataset}:shard: （Lua 内部拼接 shardId）
     * <p>
     * ARGV[1] = data            — 写入的数据内容
     * ARGV[2] = ttlSeconds      — 过期时间（秒）
     * ARGV[3] = maxMembersSize  — 每个分片的最大成员数
     * <p>
     * 执行流程：
     * 1. INCR offsetKey 获取全局唯一 offset
     * 2. 根据 offset 计算 shardId，拼接真实 shardKey
     * 3. ZADD 写入 ZSET（score = offset）
     */
    private final static RedisScript<Long> WRITE_INCREMENTAL_SCRIPT = RedisScript.of(
            "local offsetKey = KEYS[1];" +
            "local shardPrefix = KEYS[2];" +

            "local data = ARGV[1];" +
            "local ttlSeconds = tonumber(ARGV[2]);" +
            "local maxMembersSize = tonumber(ARGV[3]);" +

            // 第一步：原子递增获取全局唯一 offset
            "local offset = redis.call('INCR', offsetKey);" +

            // 第二步：根据 offset 计算 shardId，拼接真实 shardKey
            // shardId = (offset - 1) / maxMembersSize，offset 从1开始所以先-1再整除
            "local shardId = math.floor((offset - 1) / maxMembersSize);" +
            "local realShardKey = shardPrefix .. shardId;" +

            // 第三步：写入 ZSET（score = offset，value = data）
            "redis.call('ZADD', realShardKey, offset, data);" +
            "redis.call('EXPIRE', realShardKey, ttlSeconds);" +

            "return offset;",
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
     * 增量任务写入（Lua 原子方案）
     * <p>
     * INCR + ZADD 在 Lua 脚本中原子执行，保证 offset 分配与数据写入的原子性。
     * timeOffsetKey 不需要原子保证，单独写入即可。
     * </p>
     *
     * @param data       写入的数据内容
     * @param datasetName datasetName
     */
    public void write(String data, String datasetName) {
        IncrementalExecutionConfig executionConfig = snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Long ttlSeconds = incrementalConfig.getTtlSeconds();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        LocalDateTime now = LocalDateTime.now();
        Duration ttl = Duration.ofSeconds(ttlSeconds);

        // ========== 第一步：检查 offset 是否需要从 DB 恢复 ==========
        // 首次启动时 offsetKey 不存在，需要先初始化再执行 Lua 写入
        String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
        Boolean offsetExists = redisTemplate.hasKey(offsetKey);
        if (Boolean.FALSE.equals(offsetExists)) {
            initOffsetFromDb(datasetName, offsetKey);
        }

        // ========== 第二步：准备 Lua 脚本参数 ==========
        // KEYS：offsetKey、shardKey前缀（Lua内拼接shardId）
        String shardPrefix = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY.format(datasetName, "");
        List<String> keys = Arrays.asList(offsetKey, shardPrefix);

        // ARGV：data、ttlSeconds、maxMembersSize
        List<String> args = Arrays.asList(
                data,
                String.valueOf(ttlSeconds),
                String.valueOf(maxMembersSize)
        );

        // ========== 第三步：执行 Lua 原子写入（INCR + ZADD） ==========
        Long offset = redisTemplate.execute(WRITE_INCREMENTAL_SCRIPT, keys, args);
        log.info("[incrWrite] dataset={}, offset={}, data={}", datasetName, offset, data);

        // ========== 第四步：单独写入时间索引（非原子，不影响正确性） ==========
        // timeOffsetKey 只用于首次定位起始 offset，同一分钟内后写入的 offset 覆盖前值符合预期
        String minuteKey = executionConfig.getIncrIntervalMinuteKey(now);
        String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, minuteKey);
        redisTemplate.opsForValue().set(timeOffsetKey, String.valueOf(offset), ttl);

        // todo 将 offset 写入数据库（异步或定期批量）
    }

    /**
     * 从数据库恢复 offset 初始化
     * <p>
     * 当 Redis 中 offsetKey 不存在时（如首次启动或 Redis 重置），
     * 从 DB 加载最新 offset 并 SET 到 Redis，后续 Lua 脚本从该值继续 INCR。
     * </p>
     *
     * @param datasetName datasetName
     * @param offsetKey   offsetKey
     */
    private void initOffsetFromDb(String datasetName, String offsetKey) {
        String lockKey = "lock:offset:init:" + datasetName;
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", Duration.ofSeconds(10));
        if (Boolean.FALSE.equals(locked)) {
            throw new RuntimeException("Interrupted while waiting for offset init");
        }
        try {
            // 从 DB 读取最新 offset
            // Long dbOffset = offsetRepository.getCurrentOffsetFromDb(datasetName);
            // redisTemplate.opsForValue().set(offsetKey, String.valueOf(dbOffset));
            // 首次无数据时默认从 0 开始，Lua INCR 后第一个 offset 为 1
            redisTemplate.opsForValue().set(offsetKey, "0");
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

}