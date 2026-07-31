package com.healerjean.proj.hotcache.service.pull;

import com.google.gson.Gson;
import com.healerjean.proj.hotcache.config.IncrementalExecutionConfig;
import com.healerjean.proj.hotcache.config.SnapshotGlobalConfig;
import com.healerjean.proj.hotcache.enums.SnapshotPathEnum;
import com.healerjean.proj.hotcache.model.SnapshotMetadata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 增量拉取（时间延迟方案）
 * <p>
 * 拉取模型：
 *   - 只处理 now - PULL_DELAY_MINUTES 之前的分钟（该分钟所有 writer 已完成 SET timeOffset）
 *   - 按分钟推进 cursor：GET timeOffset[minute] 拿该分钟 max offset，从 lastOffset+1 逐个 GET data
 *   - 数据本体不落内存，通过 {@link IncrementalDataHandler} 交给业务方处理
 * <p>
 * cursor 状态语义：
 *   - lastMinute: 已完全消化的最后一分钟（该分钟所有数据已 handle 完），下次从 lastMinute+1 分钟继续
 *   - lastOffset: 已处理到的最后 offset，下次 GET data 从 lastOffset+1 开始
 *
 * @author zhangyujin
 * @date 2025/11/6
 */
@Service
@Slf4j
public class IncrementLoadToMemoryService {

    /**
     * 拉取延迟：只处理 now - PULL_DELAY_MINUTES 之前的分钟
     * 保证该分钟内所有 writer 已完成 SET timeOffset
     */
    private static final long PULL_DELAY_MINUTES = 2L;

    private static final DateTimeFormatter MINUTE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    @Autowired
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private IncrementalDataHandler dataHandler;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 每个 dataset 的推进状态：已完全消化的分钟 + 该分钟处理到的最后 offset
     */
    private final Map<String, ReaderCursor> LOCAL_CURSOR = new ConcurrentHashMap<>();

    /**
     * 增量拉取入口
     * <p>
     * cursor 未初始化时从快照元数据定位起点，否则从上次留下的 cursor 继续推进。
     *
     * @param datasetName datasetName
     */
    public void pullIncremental(String datasetName) {

        // ========== 第一步：初始化 cursor（首次调用时从快照元数据定位）==========
        ReaderCursor cursor = LOCAL_CURSOR.get(datasetName);
        if (cursor == null) {
            cursor = initCursorFromSnapshot(datasetName);
            if (cursor == null) {
                return;
            }
            LOCAL_CURSOR.put(datasetName, cursor);
            log.info("[pullIncr] cursor initialized. dataset={} minute={} offset={}",
                    datasetName, cursor.getLastMinute(), cursor.getLastOffset());
        }

        IncrementalExecutionConfig executionConfig = snapshotGlobalConfig.instanceIncrementalConfig(datasetName);

        // ========== 第二步：拉取窗口 = now - PULL_DELAY_MINUTES ==========
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(PULL_DELAY_MINUTES).withSecond(0).withNano(0);

        // 从"已消化的最后一分钟 + 1"开始探测
        LocalDateTime probe = LocalDateTime.parse(cursor.getLastMinute(), MINUTE_FORMATTER).plusMinutes(1);
        long processedOffset = cursor.getLastOffset();
        String lastMinute = cursor.getLastMinute();

        // ========== 第三步：按分钟推进 ==========
        while (!probe.isAfter(cutoff)) {
            String minuteKey = executionConfig.getIncrIntervalMinuteKey(probe);
            String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, minuteKey);
            String maxOffsetStr = redisTemplate.opsForValue().get(timeOffsetKey);

            // 该分钟无写入 → 分钟直接推进，offset 不动
            if (maxOffsetStr == null) {
                lastMinute = minuteKey;
                probe = probe.plusMinutes(1);
                continue;
            }

            long maxOffset = Long.parseLong(maxOffsetStr);

            // 跨分钟乱序：该分钟 max offset 小于等于已处理 → 数据早已在前一分钟拉过，跳过
            if (maxOffset <= processedOffset) {
                lastMinute = minuteKey;
                probe = probe.plusMinutes(1);
                continue;
            }

            // ========== 第四步：逐 offset 拉数据，交给 handler 处理 ==========
            for (long n = processedOffset + 1; n <= maxOffset; n++) {
                String dataKey = SnapshotPathEnum.REDIS_INCR_DATA_KEY.format(datasetName, n);
                String data = redisTemplate.opsForValue().get(dataKey);
                if (data == null) {
                    // 悬空号（writer INCR 后 crash / 超时未落盘）
                    log.warn("[pullIncr] data missing (dangling offset), dataset={} offset={}", datasetName, n);
                    continue;
                }
                dataHandler.handle(datasetName, n, data);
            }

            processedOffset = maxOffset;
            lastMinute = minuteKey;
            log.info("[pullIncr] minute done. dataset={} minute={} maxOffset={}",
                    datasetName, minuteKey, maxOffset);

            probe = probe.plusMinutes(1);
        }

        // ========== 第五步：保存推进状态 ==========
        cursor.setLastMinute(lastMinute);
        cursor.setLastOffset(processedOffset);
    }

    /**
     * 从快照元数据初始化 cursor
     * <p>
     * 起点 = 快照元数据里的 incrPullTimeLowerBound 分钟（往后找第一个有 timeOffset 的分钟）
     * 找到的 offset 对应数据已在全量快照中，读方从 lastMinute+1 分钟、offset+1 开始拉增量
     *
     * @param datasetName datasetName
     * @return {@link ReaderCursor} 初始 cursor；快照未就绪时返回 null
     */
    private ReaderCursor initCursorFromSnapshot(String datasetName) {
        String latestKey = SnapshotPathEnum.REDIS_SNAPSHOT_LATEST_VERSION_KEY.format(datasetName);
        String latestVersion = redisTemplate.opsForValue().get(latestKey);
        if (latestVersion == null) {
            log.warn("[pullIncr] No full snapshot for dataset={}, will retry next round", datasetName);
            return null;
        }

        String metaKey = SnapshotPathEnum.REDIS_SNAPSHOT_VERSION_META_KEY.format(datasetName, latestVersion);
        String metaJson = redisTemplate.opsForValue().get(metaKey);
        SnapshotMetadata metadata = new Gson().fromJson(metaJson, SnapshotMetadata.class);
        String searchStartCycleKey = metadata.getIncrPullTimeLowerBound();

        IncrementalExecutionConfig executionConfig = snapshotGlobalConfig.instanceIncrementalConfig(datasetName);
        LocalDateTime searchTime = LocalDateTime.parse(searchStartCycleKey, MINUTE_FORMATTER);
        LocalDateTime probe = searchTime;

        while (true) {
            String probeMinuteKey = executionConfig.getIncrIntervalMinuteKey(probe);
            String timeOffsetKey = SnapshotPathEnum.REDIS_INCR_TIME_OFFSET_KEY.format(datasetName, probeMinuteKey);
            String offsetValue = redisTemplate.opsForValue().get(timeOffsetKey);
            if (offsetValue != null) {
                long foundOffset = Long.parseLong(offsetValue);
                log.info("[pullIncr] Snapshot cursor found. dataset={} minute={} offset={}",
                        datasetName, probeMinuteKey, foundOffset);
                // 该分钟数据已在全量快照中，cursor 停在此分钟，后续 pullIncremental 从下一分钟开始
                return new ReaderCursor(probeMinuteKey, foundOffset);
            }

            // 探到当前分钟仍无数据 → 停下，cursor 落在时间下界，offset=0，等下一轮
            String nowCycleKey = executionConfig.getIncrIntervalMinuteKey(LocalDateTime.now());
            if (probeMinuteKey.compareTo(nowCycleKey) >= 0) {
                log.info("[pullIncr] No offset yet for dataset={}, cursor starts at minute={}",
                        datasetName, searchStartCycleKey);
                return new ReaderCursor(searchStartCycleKey, 0L);
            }
            probe = probe.plusMinutes(1);
        }
    }

    /**
     * 读方推进状态：已完全消化的最后一分钟 + 该分钟处理到的最后 offset
     */
    @Data
    @AllArgsConstructor
    private static class ReaderCursor {
        /**
         * 已完全消化的最后一分钟（yyyyMMddHHmm），下次从此分钟+1 继续拉
         */
        private String lastMinute;
        /**
         * 已处理到的最后 offset，下次从 lastOffset+1 开始 GET data
         */
        private long lastOffset;
    }

}
