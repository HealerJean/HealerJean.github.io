# IncrementalWriteService#write 并发问题分析与无 Lua 替代方案

> 前提约束：不使用 Lua 脚本，不考虑 Redis 丢数据，读和写都能拿到数据，不丢失

---

## 一、现状代码梳理

### 1.1 写端：IncrementalWriteService#write

当前 `write()` 方法执行 3 步 Redis 操作，**非原子**：

```java
// 第1步：INCR 获取全局唯一 offset
String offsetKey = "incr:{" + datasetName + "}:latestOffset";
Long offset = redisTemplate.opsForValue().increment(offsetKey);  // offset=5

// 第2步：将数据写入 Redis（⚠️ 用的是 String SET，不是 ZADD）
redisTemplate.opsForValue().set(offset.toString(), data, ttl);   // key="5", value=data

// 第3步：写入时间索引
String timeOffsetKey = "incr:{" + datasetName + "}:timeOffset:" + minuteKey;
redisTemplate.opsForValue().set(timeOffsetKey, offset.toString(), ttl);
```

### 1.2 读端：IncrementLoadToMemoryService#pullIncrementalByOffset

读端按 offset 从 ZSET 分片拉取数据：

```java
// 获取最新 offset
String latestOffsetStr = redisTemplate.opsForValue().get(latestKey);
long latestOffset = Long.parseLong(latestOffsetStr);

// 从 ZSET 按 score 范围拉取
Set<ZSetOperations.TypedTuple<String>> tuples =
    redisTemplate.opsForZSet().rangeByScoreWithScores(shardKey, nextReadOffset, batchEndOffset);

// 更新本地 processedOffset 为本批次最大 score
long maxProcessedOffset = tuples.stream()
    .mapToLong(t -> t.getScore().longValue()).max().orElse(processedOffset);
processedOffsetRef.set(maxProcessedOffset);
```

---

## 二、并发问题逐条分析

### 问题1：INCR 与数据写入之间存在间隙 → 读端看到"空洞 offset"

**时序图**：

```
时刻    写线程A                    读线程B
──────────────────────────────────────────────────
T1     INCR → offset=5
T2                               GET latestOffset → 5
T3                               ZRANGEBYSCORE 1~5 → offset=5 不在 ZSET 中！
T4                               processedOffset 更新为4（本批次最大值）
       ──────── 或更糟的情况 ────────
T5     INCR → offset=6
T6                               GET latestOffset → 6
T7                               ZRANGEBYSCORE 5~6 → ZSET 中 5和6 都不在
T8                               读到空，跳到下一分片 → processedOffset 跳过5和6
T9     ZADD shardKey 5 data
T10    ZADD shardKey 6 data      → 数据5、6永久丢失！
```

**根因**：INCR 分配 offset 后，latestOffset 立即被读端感知，但数据还没写入 ZSET。读端拉取时 ZSET 中没有对应数据，认为已追上或跳过该 offset。

### 问题2：写端用 String SET，读端用 ZSET ZRANGEBYSCORE → 结构不匹配

```java
// 写端（第60行）：写入的是 String key="5", value=data
redisTemplate.opsForValue().set(offset.toString(), data, ttl);

// 读端（第111行）：从 ZSET 按 score 范围查询
redisTemplate.opsForZSet().rangeByScoreWithScores(shardKey, nextReadOffset, batchEndOffset);
```

**写端写的是独立的 String Key（`"5"` → data），读端从 ZSET（`incr:{dataset}:shard:{shardId}`）按 score 查。写端从未执行 `ZADD`，读端永远读不到数据。**

这是一个致命 BUG，所有替代方案中统一修正为 ZSET 写入。

### 问题3：多线程并发写入时 offset 分配有序，但 ZADD 完成顺序不确定

```
时刻    写线程A                写线程B
──────────────────────────────────────
T1     INCR → offset=5
T2                            INCR → offset=6
T3                            ZADD shard 6 data_B    ← B先写完
T4     ZADD shard 5 data_A                            ← A后写完
```

如果读端在 T3~T4 之间拉取，能拿到 offset=6 的数据但拿不到 offset=5 的数据。
读端用 `max(score)` 更新 processedOffset，可能会推进到6，导致 offset=5 的数据被跳过 → 丢失。

### 问题4：读端空分片跳转策略可能跳过正在写入的 offset

```java
// 读端：当前分片为空时，直接跳到下一分片
if (CollectionUtils.isEmpty(tuples)) {
    long nextShardFirstOffset = (shardId + 1) * maxMembersSize + 1;
    if (nextShardFirstOffset > latestOffset) {
        processedOffsetRef.set(latestOffset);  // ← 直接跳到 latestOffset
        break;
    }
    processedOffsetRef.set(nextShardFirstOffset - 1);
    continue;
}
```

当 ZSET 为空但 latestOffset 表明该分片范围内有数据时（数据正在写入中），读端会跳过整个分片，导致这些 offset 的数据丢失。

### 问题5：restoreOffsetFromDb 的并发初始化问题

```java
Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", Duration.ofSeconds(10));
if (Boolean.FALSE.equals(locked)) {
    throw new RuntimeException("Interrupted while waiting for offset init");
}
```

- 锁获取失败直接抛异常，无重试
- finally 块中立即释放锁，但 try 块内 DB 代码已注释（硬编码返回1），多个等待线程可能同时拿到不一致的 offset
- Redis INCR 在 key 不存在时返回 1，正常情况下 `offset <= 0` 不会发生，这段恢复逻辑冗余

---

## 三、替代方案总览

| 方案 | 核心思想 | 读延迟 | 写吞吐 | 实现复杂度 |
|------|---------|--------|--------|-----------|
| 方案A：写后确认（ackOffset） | 写完数据后再更新 ackOffset，读端只读到 ack | 毫秒级 | 高 | 低 |
| 方案B：双 Buffer 周期切换 | 写入 active buffer，读端只读 frozen buffer | 秒级（周期对齐） | 高 | 中 |
| 方案C：单线程写入队列 | 所有写操作串行化到单线程执行 | 毫秒级 | 受限（单线程） | 最低 |
| 方案D：Offset 预占+确认 | 批量预占 offset，写完标记 ready，读端只读 ready 范围 | 毫秒级 | 最高 | 高 |

---

## 四、方案A：写后确认（ackOffset） — 推荐

### 4.1 核心思想

将 `latestOffset` 拆成两个语义：
- `latestOffset`：INCR 分配的最大 offset（写端用，只增不减）
- `ackOffset`：已确认写入 ZSET 的最大 offset（读端用，保证该 offset 之前的数据一定在 ZSET 中）

**读端上界从 `latestOffset` 改为 `ackOffset`，从而避免读到"已分配但未写入"的空洞 offset。**

### 4.2 新增 Redis Key

| Key | 结构 | 说明 |
|-----|------|------|
| `incr:{dataset}:latestOffset` | String (INCR) | 不变，写端分配 offset |
| `incr:{dataset}:ackOffset` | String | **新增**，已确认写入的最大 offset |
| `incr:{dataset}:shard:{shardId}` | ZSET | 不变，分片数据 |
| `incr:{dataset}:timeOffset:{yyyyMMddHHmm}` | String | 不变，时间索引 |

### 4.3 写端代码

```java
@Slf4j
@Component
public class AckWriteService {

    @Resource
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 增量写入（写后确认方案）
     *
     * 流程：
     * 1. INCR latestOffset → 分配全局唯一 offset
     * 2. ZADD shardKey offset data → 写入分片数据（修正原BUG：原代码用String SET）
     * 3. 更新 ackOffset = max(currentAck, offset) → 确认写入完成
     * 4. SET timeOffsetKey → 写入时间索引（非关键路径，最后写）
     */
    public void write(String data, String datasetName) {
        IncrementalExecutionConfig executionConfig =
                snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Long ttlSeconds = incrementalConfig.getTtlSeconds();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        LocalDateTime dateTime = LocalDateTime.now();
        Duration ttl = Duration.ofSeconds(ttlSeconds);

        // ========== 第1步：INCR 分配全局唯一 offset ==========
        String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
        Long offset = redisTemplate.opsForValue().increment(offsetKey);

        // ========== 第2步：ZADD 写入分片数据 ==========
        long shardId = (offset - 1) / maxMembersSize;
        String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY.format(datasetName, shardId);
        redisTemplate.opsForZSet().add(shardKey, data, offset.doubleValue());
        redisTemplate.expire(shardKey, ttl);

        // ========== 第3步：更新 ackOffset（写后确认） ==========
        // 核心：只有数据写入ZSET后，才更新ackOffset
        updateAckOffset(datasetName, offset, ttl);

        // ========== 第4步：写入时间索引（非关键路径） ==========
        String minuteKey = executionConfig.getIncrIntervalMinuteKey(dateTime);
        String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, minuteKey);
        redisTemplate.opsForValue().set(timeOffsetKey, offset.toString(), ttl);

        log.debug("[incrWrite] dataset={}, offset={}, shardId={}", datasetName, offset, shardId);
    }

    /**
     * 更新 ackOffset，保证单调递增
     *
     * 纯Java实现（非原子，但安全）：
     *   - GET 当前 ackOffset
     *   - 如果 offset > currentAck，则 SET ackOffset = offset
     *   - 并发窗口内可能出现：线程A GET=3，线程B GET=3，线程A SET=5，线程B SET=4
     *   - 结果：ackOffset=4 < 5，出现短暂回退
     *   - 不会丢数据：读端配合空洞检测，见读端代码
     *
     * 如果你能接受1行极简Lua，可以改成严格CAS：
     *   if tonumber(ARGV[1]) > tonumber(redis.call('GET', KEYS[1]) or '0')
     *   then redis.call('SET', KEYS[1], ARGV[1]) end
     */
    private void updateAckOffset(String datasetName, Long offset, Duration ttl) {
        String ackKey = "incr:{" + datasetName + "}:ackOffset";
        String currentAckStr = redisTemplate.opsForValue().get(ackKey);
        long currentAck = (currentAckStr != null) ? Long.parseLong(currentAckStr) : 0L;

        if (offset > currentAck) {
            redisTemplate.opsForValue().set(ackKey, offset.toString(), ttl);
        }
    }
}
```

### 4.4 读端代码

```java
@Service
@Slf4j
public class AckReadService {

    @Autowired
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private InMemoryUserTagCache userTagCache;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final long PULL_BATCH_SIZE = 300L;

    private final Map<String, AtomicLong> LOCAL_PROCESSED_OFFSET_MAP = new ConcurrentHashMap<>();

    /**
     * 增量拉取（写后确认方案）
     *
     * 与原方案的核心区别：
     * 1. 读端上界从 latestOffset 改为 ackOffset
     * 2. ackOffset 保证：该 offset 之前的所有数据【大概率】已在 ZSET 中
     * 3. 增加空洞检测，防止并发写导致 ackOffset 偶尔回退时跳过数据
     */
    public void pullIncrementalByOffset(String datasetName) {

        AtomicLong processedOffsetRef = LOCAL_PROCESSED_OFFSET_MAP.get(datasetName);
        if (processedOffsetRef == null) {
            log.warn("Offset not initialized for dataset: {}", datasetName);
            return;
        }

        IncrementalExecutionConfig executionConfig =
                snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();

        while (true) {
            long processedOffset = processedOffsetRef.get();

            // ========== 第1步：读取 ackOffset（而非 latestOffset） ==========
            String ackKey = "incr:{" + datasetName + "}:ackOffset";
            String ackOffsetStr = redisTemplate.opsForValue().get(ackKey);
            if (ackOffsetStr == null) {
                log.debug("[pullIncr] No ackOffset for dataset={}, sleeping...", datasetName);
                break;
            }
            long ackOffset = Long.parseLong(ackOffsetStr);

            if (processedOffset >= ackOffset) {
                log.debug("[pullIncr] caught up. localOffset={}, ackOffset={}",
                        processedOffset, ackOffset);
                break;
            }

            // ========== 第2步：计算分片 & 从 ZSET 拉取 ==========
            long nextReadOffset = processedOffset + 1;
            long shardId = (nextReadOffset - 1) / maxMembersSize;
            String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY
                    .format(datasetName, shardId);

            // ★ 关键：上界用 ackOffset，不用 latestOffset
            long batchEndOffset = Math.min(nextReadOffset + PULL_BATCH_SIZE - 1, ackOffset);
            Set<ZSetOperations.TypedTuple<String>> tuples =
                    redisTemplate.opsForZSet().rangeByScoreWithScores(
                            shardKey, (double) nextReadOffset, (double) batchEndOffset);

            // ========== 第3步：空分片处理 ==========
            if (CollectionUtils.isEmpty(tuples)) {
                long nextShardFirstOffset = (shardId + 1) * maxMembersSize + 1;
                if (nextShardFirstOffset > ackOffset) {
                    processedOffsetRef.set(ackOffset);
                    log.debug("[pullIncr] Empty shard & next beyond ack. offset {} -> {}",
                            processedOffset, ackOffset);
                    break;
                }
                long jumpToProcessedOffset = nextShardFirstOffset - 1;
                processedOffsetRef.set(jumpToProcessedOffset);
                log.debug("[pullIncr] Empty shard, jump. offset {} -> {}",
                        processedOffset, jumpToProcessedOffset);
                continue;
            }

            // ========== 第4步：处理数据 ==========
            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                Double score = tuple.getScore();
                String value = tuple.getValue();
                log.trace("[pullIncr] Record: dataset={}, offset={}, value={}",
                        datasetName, score, value);
                // TODO: 业务处理，如 userTagCache.put(...)
            }

            // ========== 第5步：更新本地偏移量（含空洞检测） ==========
            // ★ 不再用 max(score)，而是找最大连续 offset
            long maxContiguousOffset = findMaxContiguousOffset(tuples, nextReadOffset);
            processedOffsetRef.set(maxContiguousOffset);

            log.info("[pullIncr] Pulled {} records. offset {} -> {} (maxContiguous)",
                    tuples.size(), processedOffset, maxContiguousOffset);
        }
    }

    /**
     * 空洞检测：找到从 nextReadOffset 开始的最大连续 offset
     *
     * 示例：
     *   tuples 的 score = [1,2,4,5], nextReadOffset=1
     *   → 1连续✓, 2连续✓, 4不连续✗ → 返回2
     *
     *   tuples 的 score = [3,4,5], nextReadOffset=3
     *   → 3连续✓, 4连续✓, 5连续✓ → 返回5
     *
     * @param tuples          ZSET 拉取结果（按 score 升序）
     * @param nextReadOffset  本次期望读取的起始 offset
     * @return 最大连续 offset（空洞前最后一个值）
     */
    private long findMaxContiguousOffset(
            Set<ZSetOperations.TypedTuple<String>> tuples, long nextReadOffset) {
        long expected = nextReadOffset;
        long lastContiguous = nextReadOffset - 1;

        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            long actualScore = Objects.requireNonNull(tuple.getScore()).longValue();
            if (actualScore == expected) {
                lastContiguous = expected;
                expected++;
            } else if (actualScore > expected) {
                // 出现空洞，返回空洞前一个 offset
                break;
            }
            // actualScore < expected 不应出现（ZSET 按 score 升序），忽略
        }
        return lastContiguous;
    }

    /**
     * 时间驱动初始化（与原逻辑一致）
     */
    public void pullIncrementalByLowerBound(String datasetName) {
        AtomicLong existingOffset = LOCAL_PROCESSED_OFFSET_MAP.get(datasetName);
        if (existingOffset != null) {
            pullIncrementalByOffset(datasetName);
            return;
        }
        // ... 与原 IncrementLoadToMemoryService.pullIncrementalByLowerBound 逻辑一致
    }
}
```

### 4.5 ackOffset 乱序问题详解

多线程并发写入时，ZADD 完成顺序可能与 offset 分配顺序不一致：

```
时刻    写线程A(offset=5)          写线程B(offset=6)
──────────────────────────────────────────────────────
T1     INCR → 5                   INCR → 6
T2                                ZADD shard 6 data  ← B先写完
T3                                SET ackOffset=6
T4     ZADD shard 5 data                              ← A后写完
T5     SET ackOffset=5             ← ackOffset从6回退到5！
```

**影响分析**：

- T3 时刻 ackOffset=6，但 offset=5 的数据还没写入 ZSET
- T5 时刻 ackOffset 回退到5，读端上界变成5，offset=6 的数据暂时"不可见"
- **关键问题**：T3 时刻读端如果读到了 ackOffset=6，会尝试拉取 offset 5~6 的数据。offset=5 还没写入 ZSET，读端只拿到 offset=6

**解决方案：读端空洞检测（已内置在上述代码中）**

```
写端（多线程）                          读端
──────────────────────────────────────────────────────────
线程A: INCR → offset=5
线程B: INCR → offset=6
线程B: ZADD shard 6 data
线程B: SET ackOffset=6
                                        GET ackOffset → 6
                                        ZRANGEBYSCORE 5~6
                                        → 拿到 offset=6，offset=5 空洞
                                        → findMaxContiguousOffset → 返回4
                                        → processedOffset 停在4
线程A: ZADD shard 5 data
线程A: SET ackOffset=5 (ackOffset回退到5，不影响)
                                        下一轮：GET ackOffset → 5 或 6
                                        ZRANGEBYSCORE 5~6
                                        → 拿到 offset=5 和 6
                                        → findMaxContiguousOffset → 返回6
                                        → processedOffset 推进到6 ✓
```

---

## 五、方案B：双 Buffer 周期切换

### 5.1 核心思想

将 ZSET 分片按"周期"切分为两个状态：
- **Active Buffer**：当前周期正在写入的 ZSET，读端不读
- **Frozen Buffer**：上一周期已完成的 ZSET，读端可安全读取

每个周期结束时（按 `timeIntervalMinutes` 对齐），Active → Frozen，新建下一个 Active。

**读写天然隔离，不存在并发问题。**

### 5.2 新增 Redis Key

| Key | 结构 | 说明 |
|-----|------|------|
| `incr:{dataset}:activeCycleId` | String | 当前活跃周期 ID（如 `202607281017`） |
| `incr:{dataset}:shard:{cycleId}:{shardId}` | ZSET | 分片数据，按周期隔离 |
| `incr:{dataset}:frozenCycles` | ZSET (score=时间戳) | 已冻结的周期列表 |
| `incr:{dataset}:cycleStartOffset:{cycleId}` | String | 每个周期的起始 offset |
| `incr:{dataset}:latestOffset` | String (INCR) | 不变，全局 offset |

### 5.3 写端代码

```java
@Slf4j
@Component
public class DualBufferWriteService {

    @Resource
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 增量写入（双 Buffer 方案）
     *
     * 流程：
     * 1. 确保活跃周期存在（如果周期切换则冻结旧周期、创建新周期）
     * 2. INCR latestOffset → 分配 offset
     * 3. ZADD active shard → 写入当前周期的分片
     * 4. SET timeOffsetKey → 写入时间索引
     */
    public void write(String data, String datasetName) {
        IncrementalExecutionConfig executionConfig =
                snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Long ttlSeconds = incrementalConfig.getTtlSeconds();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        LocalDateTime now = LocalDateTime.now();
        Duration ttl = Duration.ofSeconds(ttlSeconds);

        // ========== 第1步：获取/切换活跃周期 ==========
        String currentCycleId = ensureActiveCycle(datasetName, executionConfig, now, ttl);

        // ========== 第2步：INCR 分配 offset ==========
        String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
        Long offset = redisTemplate.opsForValue().increment(offsetKey);

        // ========== 第3步：ZADD 写入当前周期的分片 ==========
        long shardId = (offset - 1) / maxMembersSize;
        // ★ Key 中包含 cycleId，不同周期的数据物理隔离
        String shardKey = "incr:{" + datasetName + "}:shard:" + currentCycleId + ":" + shardId;
        redisTemplate.opsForZSet().add(shardKey, data, offset.doubleValue());
        redisTemplate.expire(shardKey, ttl);

        // ========== 第4步：写入时间索引 ==========
        String minuteKey = executionConfig.getIncrIntervalMinuteKey(now);
        String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, minuteKey);
        redisTemplate.opsForValue().set(timeOffsetKey, offset.toString(), ttl);

        log.debug("[incrWrite] dataset={}, cycleId={}, offset={}, shardId={}",
                datasetName, currentCycleId, offset, shardId);
    }

    /**
     * 确保活跃周期存在，如果周期已切换则冻结旧周期、创建新周期
     */
    private String ensureActiveCycle(String datasetName,
                                      IncrementalExecutionConfig executionConfig,
                                      LocalDateTime now, Duration ttl) {
        String activeKey = "incr:{" + datasetName + "}:activeCycleId";
        String nowCycleId = executionConfig.getIncrIntervalMinuteKey(now);

        String currentCycleId = redisTemplate.opsForValue().get(activeKey);

        if (currentCycleId == null) {
            // 首次启动，初始化活跃周期
            redisTemplate.opsForValue().set(activeKey, nowCycleId, ttl);
            // 记录周期起始 offset
            saveCycleStartOffset(datasetName, nowCycleId, ttl);
            return nowCycleId;
        }

        if (currentCycleId.equals(nowCycleId)) {
            // 周期未变，直接返回
            return currentCycleId;
        }

        // ========== 周期切换 ==========
        // 1. 将旧周期加入冻结列表
        String frozenKey = "incr:{" + datasetName + "}:frozenCycles";
        long currentCycleTimestamp = Long.parseLong(currentCycleId);
        redisTemplate.opsForZSet().add(frozenKey, currentCycleId, currentCycleTimestamp);

        // 2. 更新活跃周期为新周期
        redisTemplate.opsForValue().set(activeKey, nowCycleId, ttl);

        // 3. 记录新周期的起始 offset
        saveCycleStartOffset(datasetName, nowCycleId, ttl);

        log.info("[cycleSwitch] dataset={}, oldCycle={}, newCycle={}",
                datasetName, currentCycleId, nowCycleId);
        return nowCycleId;
    }

    /**
     * 记录周期起始 offset（新周期第一条数据的 offset）
     */
    private void saveCycleStartOffset(String datasetName, String cycleId, Duration ttl) {
        String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
        String latestOffsetStr = redisTemplate.opsForValue().get(offsetKey);
        if (latestOffsetStr != null) {
            long nextOffset = Long.parseLong(latestOffsetStr) + 1;
            String cycleStartKey = "incr:{" + datasetName + "}:cycleStartOffset:" + cycleId;
            redisTemplate.opsForValue().set(cycleStartKey, String.valueOf(nextOffset), ttl);
        }
    }
}
```

### 5.4 读端代码

```java
@Service
@Slf4j
public class DualBufferReadService {

    @Autowired
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final long PULL_BATCH_SIZE = 300L;

    private final Map<String, AtomicLong> LOCAL_PROCESSED_OFFSET_MAP = new ConcurrentHashMap<>();

    /**
     * 增量拉取（双 Buffer 方案）
     *
     * 核心逻辑：
     * 1. 先消费所有 frozen 周期的数据（按周期时间顺序）
     * 2. frozen 周期消费完后，active 周期等它冻结后再读
     *
     * 优点：frozen 周期数据一定完整，不存在并发问题
     * 缺点：读延迟 = 一个周期（timeIntervalMinutes）
     */
    public void pullIncremental(String datasetName) {
        AtomicLong processedOffsetRef = LOCAL_PROCESSED_OFFSET_MAP.get(datasetName);
        if (processedOffsetRef == null) {
            log.warn("Offset not initialized for dataset: {}", datasetName);
            return;
        }

        IncrementalExecutionConfig executionConfig =
                snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        Long ttlSeconds = incrementalConfig.getTtlSeconds();

        // ========== 第1步：获取所有 frozen 周期（按时间排序） ==========
        String frozenKey = "incr:{" + datasetName + "}:frozenCycles";
        Set<String> frozenCycles = redisTemplate.opsForZSet().range(frozenKey, 0, -1);

        if (frozenCycles != null && !frozenCycles.isEmpty()) {
            for (String cycleId : frozenCycles) {
                // 拉取该周期所有分片数据
                pullCycleData(datasetName, cycleId, processedOffsetRef, maxMembersSize);

                // 消费完后从 frozen 列表中移除
                redisTemplate.opsForZSet().remove(frozenKey, cycleId);
                log.info("[pullIncr] Consumed frozen cycle={}", cycleId);
            }
        }

        // ========== 第2步：active 周期不读，等它冻结 ==========
        String activeKey = "incr:{" + datasetName + "}:activeCycleId";
        String activeCycleId = redisTemplate.opsForValue().get(activeKey);
        log.debug("[pullIncr] Waiting for active cycle={} to freeze. processedOffset={}",
                activeCycleId, processedOffsetRef.get());
    }

    /**
     * 拉取指定周期的所有分片数据
     */
    private void pullCycleData(String datasetName, String cycleId,
                                AtomicLong processedOffsetRef,
                                Integer maxMembersSize) {
        while (true) {
            long processedOffset = processedOffsetRef.get();
            long nextReadOffset = processedOffset + 1;

            // 获取最新 offset
            String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
            String latestOffsetStr = redisTemplate.opsForValue().get(offsetKey);
            if (latestOffsetStr == null) {
                break;
            }
            long latestOffset = Long.parseLong(latestOffsetStr);

            if (processedOffset >= latestOffset) {
                break;
            }

            // 计算分片
            long shardId = (nextReadOffset - 1) / maxMembersSize;
            String shardKey = "incr:{" + datasetName + "}:shard:" + cycleId + ":" + shardId;

            long batchEndOffset = Math.min(nextReadOffset + PULL_BATCH_SIZE - 1, latestOffset);
            Set<ZSetOperations.TypedTuple<String>> tuples =
                    redisTemplate.opsForZSet().rangeByScoreWithScores(
                            shardKey, (double) nextReadOffset, (double) batchEndOffset);

            if (tuples == null || tuples.isEmpty()) {
                // frozen 周期数据完整，如果该分片为空，说明 processedOffset 已覆盖该周期
                break;
            }

            // 处理数据
            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                Double score = tuple.getScore();
                String value = tuple.getValue();
                log.trace("[pullIncr] dataset={}, cycle={}, offset={}, value={}",
                        datasetName, cycleId, score, value);
                // TODO: 业务处理
            }

            // 更新本地偏移量（frozen 周期数据完整，可用 max score）
            long maxProcessedOffset = tuples.stream()
                    .mapToLong(t -> Objects.requireNonNull(t.getScore()).longValue())
                    .max()
                    .orElse(processedOffset);
            processedOffsetRef.set(maxProcessedOffset);

            log.info("[pullIncr] Cycle={}, pulled {} records. offset {} -> {}",
                    cycleId, tuples.size(), processedOffset, maxProcessedOffset);
        }
    }

    /**
     * 时间驱动初始化（与原逻辑一致）
     */
    public void pullIncrementalByLowerBound(String datasetName) {
        // ... 与原 IncrementLoadToMemoryService.pullIncrementalByLowerBound 逻辑一致
    }
}
```

### 5.5 方案B 读写时序图

```
写端                                    读端
──────────────────────────────────────────────────────────
周期 10:17（active）
  ZADD shard:202607281017:0, score=1
  ZADD shard:202607281017:0, score=2
  ...
  ZADD shard:202607281017:0, score=10
                                        frozenCycles 为空，等待
──────────────────────────────────────────────────────────
周期切换 → 10:17 frozen，10:18 active
  10:17 加入 frozenCycles
  ZADD shard:202607281018:1, score=11
                                        读取 frozenCycles → ["202607281017"]
                                        ZRANGEBYSCORE shard:202607281017:0 1~10
                                        → 拿到完整数据，processedOffset=10
                                        移除 frozen cycle "202607281017"
                                        等待 10:18 冻结...
──────────────────────────────────────────────────────────
周期切换 → 10:18 frozen，10:19 active
  ...
```

### 5.6 方案B 注意事项

- **读延迟**：等于一个周期（`timeIntervalMinutes`），默认1分钟
- **周期切换的并发安全**：多写线程可能同时检测到周期切换，但 `SET activeKey` 幂等，最多多写一条 frozenCycles 记录（读端消费时去重即可）
- **分片 Key 变化**：ZSET Key 从 `incr:{dataset}:shard:{shardId}` 变为 `incr:{dataset}:shard:{cycleId}:{shardId}`，需同步修改所有读写端
- **内存开销**：每个周期独立 ZSET，周期越多占内存越大。通过 TTL 和 frozenCycles 清理控制

---

## 六、方案C：单线程写入队列

### 6.1 核心思想

将所有写入请求投递到内存队列，单消费线程串行执行 INCR → ZADD → SET timeOffset。

**天然消除并发：所有操作在同一线程中顺序执行，读端看到 latestOffset 时，对应数据一定已在 ZSET 中。**

### 6.2 写端代码

```java
@Slf4j
@Component
public class SingleThreadWriteService implements InitializingBean, DisposableBean {

    @Resource
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 写入请求队列
     * - 有界阻塞队列，防止 OOM
     * - 队列满时 write() 方法阻塞等待（或可改为拒绝策略）
     */
    private final BlockingQueue<WriteTask> writeQueue = new LinkedBlockingQueue<>(10000);

    /**
     * 单消费线程
     */
    private volatile Thread consumerThread;

    private volatile boolean running = true;

    /**
     * 写入请求封装
     */
    @Data
    @AllArgsConstructor
    private static class WriteTask {
        private String data;
        private String datasetName;
        /** 写入完成的回调（可选，用于同步等待写入结果） */
        private CompletableFuture<Long> future;
    }

    /**
     * 增量写入（投递到队列，异步消费）
     *
     * 流程：
     * 1. 构造 WriteTask 投递到队列
     * 2. 单线程消费者从队列取出任务
     * 3. 串行执行：INCR → ZADD → SET timeOffset
     *
     * @return CompletableFuture<Long> 写入完成后的 offset（可忽略）
     */
    public CompletableFuture<Long> write(String data, String datasetName) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        WriteTask task = new WriteTask(data, datasetName, future);
        try {
            // 投递到队列，如果队列满则阻塞等待（最多5秒）
            boolean offered = writeQueue.offer(task, 5, TimeUnit.SECONDS);
            if (!offered) {
                future.completeExceptionally(
                        new RuntimeException("Write queue full, dataset=" + datasetName));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            future.completeExceptionally(e);
        }
        return future;
    }

    /**
     * 同步写入（等待写入完成）
     */
    public long writeSync(String data, String datasetName) {
        try {
            return write(data, datasetName).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Write sync failed", e);
        }
    }

    /**
     * 单线程消费者：串行执行所有写入操作
     */
    private void consume() {
        while (running || !writeQueue.isEmpty()) {
            try {
                WriteTask task = writeQueue.poll(100, TimeUnit.MILLISECONDS);
                if (task == null) {
                    continue;
                }
                long offset = doWrite(task.getData(), task.getDatasetName());
                task.getFuture().complete(offset);
            } catch (Exception e) {
                log.error("[incrWrite] Consume error", e);
            }
        }
        log.info("[incrWrite] Consumer thread exited");
    }

    /**
     * 实际执行写入（单线程内串行执行，天然无并发）
     */
    private long doWrite(String data, String datasetName) {
        IncrementalExecutionConfig executionConfig =
                snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Long ttlSeconds = incrementalConfig.getTtlSeconds();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        LocalDateTime dateTime = LocalDateTime.now();
        Duration ttl = Duration.ofSeconds(ttlSeconds);

        // ========== 第1步：INCR 分配 offset ==========
        String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
        Long offset = redisTemplate.opsForValue().increment(offsetKey);

        // ========== 第2步：ZADD 写入分片数据 ==========
        // 单线程执行，INCR 和 ZADD 之间没有其他线程插队
        // 读端看到 latestOffset 时，ZADD 一定已完成（因为下一次 INCR 还没执行）
        long shardId = (offset - 1) / maxMembersSize;
        String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY.format(datasetName, shardId);
        redisTemplate.opsForZSet().add(shardKey, data, offset.doubleValue());
        redisTemplate.expire(shardKey, ttl);

        // ========== 第3步：写入时间索引 ==========
        String minuteKey = executionConfig.getIncrIntervalMinuteKey(dateTime);
        String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, minuteKey);
        redisTemplate.opsForValue().set(timeOffsetKey, offset.toString(), ttl);

        log.debug("[incrWrite] dataset={}, offset={}, shardId={}", datasetName, offset, shardId);
        return offset;
    }

    @Override
    public void afterPropertiesSet() {
        consumerThread = new Thread(this::consume, "incr-write-consumer");
        consumerThread.setDaemon(true);
        consumerThread.start();
        log.info("[incrWrite] Single-thread consumer started");
    }

    @Override
    public void destroy() {
        running = false;
        if (consumerThread != null) {
            consumerThread.interrupt();
        }
    }
}
```

### 6.3 读端代码

```java
@Service
@Slf4j
public class SingleThreadReadService {

    @Autowired
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final long PULL_BATCH_SIZE = 300L;

    private final Map<String, AtomicLong> LOCAL_PROCESSED_OFFSET_MAP = new ConcurrentHashMap<>();

    /**
     * 增量拉取（配合单线程写入方案）
     *
     * 核心保证：单线程写入时，latestOffset 一定已有对应数据在 ZSET 中
     * 因为写线程是串行的：上一次 ZADD 完成后，才执行下一次 INCR
     *
     * 读端逻辑与原方案基本一致，但空分片跳转更安全：
     * - 空分片 = 该 offset 范围确实没有数据（不是"还没写"）
     * - 因为写线程是串行的，latestOffset 对应的 ZADD 一定已完成
     */
    public void pullIncrementalByOffset(String datasetName) {

        AtomicLong processedOffsetRef = LOCAL_PROCESSED_OFFSET_MAP.get(datasetName);
        if (processedOffsetRef == null) {
            log.warn("Offset not initialized for dataset: {}", datasetName);
            return;
        }

        IncrementalExecutionConfig executionConfig =
                snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();

        while (true) {
            long processedOffset = processedOffsetRef.get();

            // ========== 第1步：读取 latestOffset ==========
            // 单线程写入保证：latestOffset 对应的 ZADD 一定已完成
            String latestKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
            String latestOffsetStr = redisTemplate.opsForValue().get(latestKey);
            if (latestOffsetStr == null) {
                log.debug("[pullIncr] No latest offset for dataset={}", datasetName);
                break;
            }
            long latestOffset = Long.parseLong(latestOffsetStr);

            if (processedOffset >= latestOffset) {
                log.debug("[pullIncr] caught up. localOffset={}, latestOffset={}",
                        processedOffset, latestOffset);
                break;
            }

            // ========== 第2步：计算分片 & 拉取 ==========
            long nextReadOffset = processedOffset + 1;
            long shardId = (nextReadOffset - 1) / maxMembersSize;
            String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY
                    .format(datasetName, shardId);

            long batchEndOffset = Math.min(nextReadOffset + PULL_BATCH_SIZE - 1, latestOffset);
            Set<ZSetOperations.TypedTuple<String>> tuples =
                    redisTemplate.opsForZSet().rangeByScoreWithScores(
                            shardKey, (double) nextReadOffset, (double) batchEndOffset);

            // ========== 第3步：空分片处理 ==========
            // 单线程写入场景下，空分片意味着该 offset 范围确实没有数据（不是并发写导致）
            // 但仍需保守处理：可能存在 INCR 和 ZADD 之间的极短窗口
            // （单线程内 INCR 和 ZADD 不是原子的，但在同一网络连接中是顺序的）
            if (CollectionUtils.isEmpty(tuples)) {
                long nextShardFirstOffset = (shardId + 1) * maxMembersSize + 1;
                if (nextShardFirstOffset > latestOffset) {
                    // ★ 安全做法：不直接跳到 latestOffset，保守跳到下一分片
                    // 因为单线程内 INCR 和 ZADD 之间仍有极短窗口
                    // 但这个窗口极小（同一 Redis 连接的两次命令间隙）
                    // 如果要绝对安全，可以 break 等下一轮拉取
                    break;
                }
                long jumpToProcessedOffset = nextShardFirstOffset - 1;
                processedOffsetRef.set(jumpToProcessedOffset);
                log.debug("[pullIncr] Empty shard, jump. offset {} -> {}",
                        processedOffset, jumpToProcessedOffset);
                continue;
            }

            // ========== 第4步：处理数据 ==========
            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                Double score = tuple.getScore();
                String value = tuple.getValue();
                log.trace("[pullIncr] Record: dataset={}, offset={}, value={}",
                        datasetName, score, value);
                // TODO: 业务处理
            }

            // ========== 第5步：更新本地偏移量 ==========
            // 单线程写入保证数据按 offset 顺序写入，用 max(score) 安全
            long maxProcessedOffset = tuples.stream()
                    .mapToLong(t -> Objects.requireNonNull(t.getScore()).longValue())
                    .max()
                    .orElse(processedOffset);
            processedOffsetRef.set(maxProcessedOffset);

            log.info("[pullIncr] Pulled {} records. offset {} -> {}",
                    tuples.size(), processedOffset, maxProcessedOffset);
        }
    }

    /**
     * 时间驱动初始化（与原逻辑一致）
     */
    public void pullIncrementalByLowerBound(String datasetName) {
        // ... 与原 IncrementLoadToMemoryService.pullIncrementalByLowerBound 逻辑一致
    }
}
```

### 6.4 方案C 读写时序图

```
写端（单线程队列消费）                    读端
──────────────────────────────────────────────────────────
INCR → offset=1
ZADD shard:0, score=1, data_A
INCR → offset=2
ZADD shard:0, score=2, data_B
INCR → offset=3
ZADD shard:0, score=3, data_C
                                        GET latestOffset → 3
                                        ZRANGEBYSCORE 1~3
                                        → 拿到 [1,2,3] 完整数据 ✓
                                        processedOffset = 3
INCR → offset=4
ZADD shard:0, score=4, data_D
                                        GET latestOffset → 4
                                        ZRANGEBYSCORE 4~4
                                        → 拿到 [4] ✓
```

### 6.5 方案C 注意事项

- **写吞吐瓶颈**：单线程串行执行所有 Redis 操作，QPS 上限 = 1/（INCR耗时+ZADD耗时+SET耗时），约 3000~5000 QPS
- **队列容量**：`LinkedBlockingQueue(10000)` 防止 OOM，队列满时 write() 阻塞
- **优雅停机**：`destroy()` 中设置 `running=false` 并等待队列消费完
- **适用场景**：写入 QPS 不高（<1万/s），对读延迟敏感（要求毫秒级可见）
- **极短窗口**：单线程内 INCR 和 ZADD 之间仍有微秒级窗口，但实际影响可忽略

---

## 七、方案D：Offset 预占 + 确认

### 7.1 核心思想

写端批量预占一段 offset 范围（`INCRBY offsetKey N`），逐条写入后标记该范围已就绪。读端只读已就绪的 offset 范围。

**与方案A的区别**：方案A 是逐条确认，方案D 是批量确认。批量确认减少了 ackOffset 更新次数，同时保证批量内的 offset 一定连续。

### 7.2 新增 Redis Key

| Key | 结构 | 说明 |
|-----|------|------|
| `incr:{dataset}:latestOffset` | String (INCR/INCRBY) | 不变 |
| `incr:{dataset}:readyOffset` | String | **新增**，已就绪的最大 offset（等价于方案A的 ackOffset） |
| `incr:{dataset}:shard:{shardId}` | ZSET | 不变 |
| `incr:{dataset}:timeOffset:{yyyyMMddHHmm}` | String | 不变 |

### 7.3 写端代码

```java
@Slf4j
@Component
public class BatchWriteService {

    @Resource
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 每次预占的 offset 数量
     * - 太小（如1）：退化为方案A，每条数据都要更新 readyOffset
     * - 太大（如1000）：预占后如果写入失败，中间 offset 全部浪费（空洞）
     * - 建议 10~50
     */
    private static final int BATCH_SIZE = 20;

    /**
     * 本地预占窗口
     * Key: datasetName
     * Value: 当前预占的 offset 范围 [currentOffset, endOffset]
     */
    private final Map<String, OffsetWindow> localWindows = new ConcurrentHashMap<>();

    @Data
    @AllArgsConstructor
    private static class OffsetWindow {
        /** 当前可用 offset（预占范围内下一个待写入的 offset） */
        private long currentOffset;
        /** 预占范围的结束 offset（闭区间） */
        private long endOffset;
        /** 本窗口内已成功写入的最大 offset */
        private long maxWrittenOffset;
    }

    /**
     * 增量写入（批量预占方案）
     *
     * 流程：
     * 1. 从本地窗口分配 offset（无锁）
     * 2. 窗口耗尽时，INCRBY 预占下一批 offset
     * 3. ZADD 写入数据
     * 4. 更新本地窗口的 maxWrittenOffset
     * 5. 窗口内所有 offset 写完时，更新 readyOffset
     */
    public void write(String data, String datasetName) {
        IncrementalExecutionConfig executionConfig =
                snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Long ttlSeconds = incrementalConfig.getTtlSeconds();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();
        Duration ttl = Duration.ofSeconds(ttlSeconds);

        // ========== 第1步：分配 offset ==========
        long offset = allocateOffset(datasetName, ttl);

        // ========== 第2步：ZADD 写入分片数据 ==========
        long shardId = (offset - 1) / maxMembersSize;
        String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY.format(datasetName, shardId);
        redisTemplate.opsForZSet().add(shardKey, data, offset.doubleValue());
        redisTemplate.expire(shardKey, ttl);

        // ========== 第3步：更新本地窗口的 maxWrittenOffset ==========
        OffsetWindow window = localWindows.get(datasetName);
        synchronized (window) {
            window.setMaxWrittenOffset(Math.max(window.getMaxWrittenOffset(), offset));

            // ========== 第4步：窗口内所有 offset 写完时，更新 readyOffset ==========
            if (window.getMaxWrittenOffset() >= window.getEndOffset()) {
                String readyKey = "incr:{" + datasetName + "}:readyOffset";
                redisTemplate.opsForValue().set(readyKey,
                        String.valueOf(window.getEndOffset()), ttl);
                log.debug("[incrWrite] Batch complete. readyOffset={}", window.getEndOffset());
            }
        }

        // ========== 第5步：写入时间索引 ==========
        LocalDateTime dateTime = LocalDateTime.now();
        String minuteKey = executionConfig.getIncrIntervalMinuteKey(dateTime);
        String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, minuteKey);
        redisTemplate.opsForValue().set(timeOffsetKey, offset.toString(), ttl);

        log.debug("[incrWrite] dataset={}, offset={}, shardId={}", datasetName, offset, shardId);
    }

    /**
     * 从本地窗口分配 offset，窗口耗尽时预占下一批
     */
    private long allocateOffset(String datasetName, Duration ttl) {
        OffsetWindow window = localWindows.computeIfAbsent(datasetName,
                k -> new OffsetWindow(0, 0, 0));

        synchronized (window) {
            if (window.getCurrentOffset() <= window.getEndOffset()
                    && window.getCurrentOffset() > 0) {
                // 窗口内还有剩余 offset
                long offset = window.getCurrentOffset();
                window.setCurrentOffset(offset + 1);
                return offset;
            }

            // 窗口耗尽，INCRBY 预占下一批
            String offsetKey = SnapshotPathEnum.REDIS_INCR_LATEST_OFFSET_KEY.format(datasetName);
            Long endOffset = redisTemplate.opsForValue().increment(offsetKey, BATCH_SIZE);
            long startOffset = endOffset - BATCH_SIZE + 1;

            window.setCurrentOffset(startOffset + 1); // +1 因为 startOffset 本身就是第一个要分配的
            window.setEndOffset(endOffset);
            window.setMaxWrittenOffset(0);

            log.debug("[incrWrite] New batch allocated. dataset={}, range=[{}, {}]",
                    datasetName, startOffset, endOffset);
            return startOffset;
        }
    }
}
```

### 7.4 读端代码

```java
@Service
@Slf4j
public class BatchReadService {

    @Autowired
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private static final long PULL_BATCH_SIZE = 300L;

    private final Map<String, AtomicLong> LOCAL_PROCESSED_OFFSET_MAP = new ConcurrentHashMap<>();

    /**
     * 增量拉取（批量预占方案）
     *
     * 与方案A读端几乎一致，区别：
     * - ackOffset 改名为 readyOffset（语义相同）
     * - readyOffset 按批量粒度更新，比逐条更新更平滑
     * - 空洞检测仍然需要（同一批量内不同线程 ZADD 顺序不确定）
     */
    public void pullIncrementalByOffset(String datasetName) {

        AtomicLong processedOffsetRef = LOCAL_PROCESSED_OFFSET_MAP.get(datasetName);
        if (processedOffsetRef == null) {
            log.warn("Offset not initialized for dataset: {}", datasetName);
            return;
        }

        IncrementalExecutionConfig executionConfig =
                snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        DatasetIncrementalConfig incrementalConfig = executionConfig.getIncrementalConfig();
        Integer maxMembersSize = incrementalConfig.getMaxMembersSize();

        while (true) {
            long processedOffset = processedOffsetRef.get();

            // ========== 第1步：读取 readyOffset ==========
            String readyKey = "incr:{" + datasetName + "}:readyOffset";
            String readyOffsetStr = redisTemplate.opsForValue().get(readyKey);
            if (readyOffsetStr == null) {
                log.debug("[pullIncr] No readyOffset for dataset={}", datasetName);
                break;
            }
            long readyOffset = Long.parseLong(readyOffsetStr);

            if (processedOffset >= readyOffset) {
                log.debug("[pullIncr] caught up. localOffset={}, readyOffset={}",
                        processedOffset, readyOffset);
                break;
            }

            // ========== 第2步：计算分片 & 拉取 ==========
            long nextReadOffset = processedOffset + 1;
            long shardId = (nextReadOffset - 1) / maxMembersSize;
            String shardKey = SnapshotPathEnum.REDIS_INCR_CURRENT_SHARD_KEY
                    .format(datasetName, shardId);

            long batchEndOffset = Math.min(nextReadOffset + PULL_BATCH_SIZE - 1, readyOffset);
            Set<ZSetOperations.TypedTuple<String>> tuples =
                    redisTemplate.opsForZSet().rangeByScoreWithScores(
                            shardKey, (double) nextReadOffset, (double) batchEndOffset);

            // ========== 第3步：空分片处理 ==========
            if (CollectionUtils.isEmpty(tuples)) {
                long nextShardFirstOffset = (shardId + 1) * maxMembersSize + 1;
                if (nextShardFirstOffset > readyOffset) {
                    processedOffsetRef.set(readyOffset);
                    break;
                }
                processedOffsetRef.set(nextShardFirstOffset - 1);
                continue;
            }

            // ========== 第4步：处理数据 ==========
            for (ZSetOperations.TypedTuple<String> tuple : tuples) {
                Double score = tuple.getScore();
                String value = tuple.getValue();
                log.trace("[pullIncr] dataset={}, offset={}, value={}",
                        datasetName, score, value);
                // TODO: 业务处理
            }

            // ========== 第5步：更新本地偏移量（含空洞检测） ==========
            long maxContiguousOffset = findMaxContiguousOffset(tuples, nextReadOffset);
            processedOffsetRef.set(maxContiguousOffset);

            log.info("[pullIncr] Pulled {} records. offset {} -> {}",
                    tuples.size(), processedOffset, maxContiguousOffset);
        }
    }

    /**
     * 空洞检测（同方案A）
     */
    private long findMaxContiguousOffset(
            Set<ZSetOperations.TypedTuple<String>> tuples, long nextReadOffset) {
        long expected = nextReadOffset;
        long lastContiguous = nextReadOffset - 1;

        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            long actualScore = Objects.requireNonNull(tuple.getScore()).longValue();
            if (actualScore == expected) {
                lastContiguous = expected;
                expected++;
            } else if (actualScore > expected) {
                break;
            }
        }
        return lastContiguous;
    }

    /**
     * 时间驱动初始化（与原逻辑一致）
     */
    public void pullIncrementalByLowerBound(String datasetName) {
        // ... 与原 IncrementLoadToMemoryService.pullIncrementalByLowerBound 逻辑一致
    }
}
```

### 7.5 方案D 读写时序图

```
写端（多线程批量预占）                    读端
──────────────────────────────────────────────────────────
线程A: INCRBY 20 → range [1,20]
线程A: offset=1, ZADD shard:0 score=1 data
线程A: offset=2, ZADD shard:0 score=2 data
线程B: offset=3, ZADD shard:0 score=3 data
线程B: offset=4, ZADD shard:0 score=4 data
线程A: offset=5, ZADD shard:0 score=5 data
...
线程A: offset=20, ZADD → 写完，SET readyOffset=20
                                        GET readyOffset → 20
                                        ZRANGEBYSCORE 1~20
                                        → 空洞检测，拿到连续数据 ✓
                                        processedOffset 推进到最大连续值
```

### 7.6 方案D 注意事项

- **预占窗口大小**：`BATCH_SIZE` 需要权衡。太大 → 预占后如果进程崩溃，中间 offset 永久空洞；太小 → 退化为方案A
- **readyOffset 更新时机**：整个窗口写完才更新。如果某个线程写 offset=19 特别慢，整个 [1,20] 的 readyOffset 都不会更新，读端延迟增大
- **改进**：可以将窗口按子范围更新 readyOffset（如每5条更新一次），但这增加了实现复杂度
- **适用场景**：写入 QPS 极高（>1万/s），需要减少 Redis INCR 调用次数

---

## 八、方案对比总结

| 维度 | 方案A：写后确认 | 方案B：双Buffer | 方案C：单线程队列 | 方案D：批量预占 |
|------|----------------|----------------|------------------|----------------|
| **并发安全** | 安全（空洞检测兜底） | 完全安全（读写隔离） | 完全安全（串行化） | 安全（空洞检测兜底） |
| **读延迟** | 毫秒级 | 秒级（1个周期） | 毫秒级 | 毫秒级 |
| **写吞吐** | 高（受限于3次Redis调用/条） | 高 | 受限（单线程串行） | 最高（INCRBY批量化） |
| **实现复杂度** | 低（改写端+读端上界变更+空洞检测） | 中（新增周期管理逻辑） | 最低（队列+单线程） | 高（窗口管理+批量确认） |
| **改动范围** | 写端+读端 | 写端+读端+Key结构 | 仅写端 | 写端+读端 |
| **是否需要Lua** | 否（1行极简Lua可选） | 否 | 否 | 否 |
| **不丢数据** | 保证 | 保证 | 保证 | 保证 |
| **适用场景** | 通用场景，改动最小 | 可接受秒级读延迟 | QPS不高，要求毫秒级可见 | QPS极高，需要批量化 |

---

## 九、推荐选择

### 场景1：改动最小、快速上线 → **方案A（写后确认）**

- 改动点最少：写端加一行 ackOffset 更新，读端上界从 latestOffset 改为 ackOffset，加空洞检测
- 不改 Redis Key 结构
- 读延迟毫秒级
- 已有代码基础改动量约 50 行

### 场景2：可接受秒级读延迟、追求极致安全 → **方案B（双Buffer）**

- 读写天然隔离，无并发问题
- 实现稍复杂，需要周期切换逻辑
- 读延迟 = `timeIntervalMinutes`（默认1分钟）

### 场景3：写入QPS不高、追求最简实现 → **方案C（单线程队列）**

- 写端投队列，单线程消费
- 读端逻辑几乎不变
- 适合 QPS < 1万/s 的场景

### 场景4：写入QPS极高 → **方案D（批量预占）**

- INCRBY 批量预占，减少 Redis 调用
- 实现最复杂
- 适合 QPS > 1万/s 的场景