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
import java.util.Collections;

/**
 * 增量数据写入（时间延迟方案）
 * <p>
 * 核心思路：读方拉取时统一延迟 2 分钟，该分钟所有 writer 早已完成写入，
 *         写侧无需应用层协调 offset 顺序，只需保证：
 *         "同一分钟内所有 writer 的 offset，最终都被记录到 timeOffset[minute] 上"。
 * <p>
 * 写侧四步：
 *   1. INCR assignedOffset -> N
 *   2. SET data:N data EX ttl
 *   3. Lua CAS-max: timeOffset[minute] = max(current, N)
 *      —— 该分钟所有 writer 抢着写 timeOffset，Lua 原子比较后只保留最大值，
 *         保证读方 GET timeOffset[minute] 拿到的一定是该分钟真实的 max offset
 *   4. ZADD dataIndex[dataId] offset -> 建立 data 反向索引（排查用，不影响主链路）
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Slf4j
@Component
public class IncrementalWriteService {

    /**
     * 反向索引每个 dataId 保留最新 N 条变更记录
     */
    private static final int DATA_INDEX_MAX_VARIANTS = 10;

    /**
     * Lua CAS-max 脚本：仅当传入 offset 大于当前值时才 SET
     * <p>
     * KEYS[1] = timeOffsetKey
     * ARGV[1] = offset
     * ARGV[2] = ttlSeconds
     * <p>
     * 解决问题：同一分钟内多个 writer 并发 SET timeOffset，后到的慢 writer 若持有较小 offset
     *          会覆盖已写入的大 offset，导致读方漏读该分钟内更大的 offset。
     *          用 Lua 原子比较后只保留 max，天然避免乱序覆盖。
     */
    private static final RedisScript<Long> TIME_OFFSET_CAS_MAX_SCRIPT = RedisScript.of(
            "local cur = redis.call('GET', KEYS[1]);" +
            "if (not cur) or (tonumber(cur) < tonumber(ARGV[1])) then " +
            "    redis.call('SET', KEYS[1], ARGV[1], 'EX', tonumber(ARGV[2]));" +
            "    return 1;" +
            "end;" +
            "return 0;",
            Long.class
    );

    @Resource
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 增量任务写入
     *
     * @param data        写入的数据内容
     * @param datasetName datasetName
     */
    public void write(String data, String datasetName) {
        IncrementalExecutionConfig executionConfig = snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Long ttlSeconds = incrementalConfig.getTtlSeconds();
        Duration ttl = Duration.ofSeconds(ttlSeconds);
        LocalDateTime now = LocalDateTime.now();

        // ========== 第一步：INCR 分配全局唯一 offset ==========
        String assignedKey = SnapshotPathEnum.REDIS_INCR_ASSIGNED_OFFSET_KEY.format(datasetName);
        Long offset = redisTemplate.opsForValue().increment(assignedKey);
        if (offset == null || offset <= 0) {
            offset = restoreOffsetFromDb(datasetName, assignedKey);
        } else {
            // todo 将 offset 写入数据库
        }

        // ========== 第二步：SET data:N 数据本体 ==========
        String dataKey = SnapshotPathEnum.REDIS_INCR_DATA_KEY.format(datasetName, offset);
        redisTemplate.opsForValue().set(dataKey, data, ttl);

        // ========== 第三步：Lua CAS-max 写时间索引 ==========
        // 该分钟所有 writer 都往 timeOffset[minute] 上写自己的 offset，Lua 保证只保留最大值
        String minuteKey = executionConfig.getIncrIntervalMinuteKey(now);
        String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, minuteKey);
        redisTemplate.execute(
                TIME_OFFSET_CAS_MAX_SCRIPT,
                Collections.singletonList(timeOffsetKey),
                String.valueOf(offset),
                String.valueOf(ttlSeconds)
        );

        // ========== 第四步：data 反向索引 ZSET（排查用，不影响主链路）==========
        // ZSET score = offset，累积同一 dataId 的多次变更；保留最新 DATA_INDEX_MAX_VARIANTS 条
        String dataId = extractDataId(data);
        String dataIndexKey = SnapshotPathEnum.REDIS_INCR_DATA_INDEX_KEY.format(datasetName, dataId);
        redisTemplate.opsForZSet().add(dataIndexKey, String.valueOf(offset), (double) offset);
        redisTemplate.opsForZSet().removeRange(dataIndexKey, 0, -(DATA_INDEX_MAX_VARIANTS + 1L));
        redisTemplate.expire(dataIndexKey, ttl);

        // todo 将 offset 写入数据库
    }

    /**
     * 从 data 中提取业务唯一标识，用于建立 data → offset 反向索引
     * demo 版本先 mock，后续接入业务字段解析
     */
    private String extractDataId(String data) {
        // todo 从 data 中解析业务 ID（如 orderId / userId / skuId）
        return "mockDataId";
    }

    /**
     * 从数据库恢复 assignedOffset（首次启动或 Redis 重置时）
     *
     * @param datasetName datasetName
     * @param offsetKey   assignedOffset key
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
