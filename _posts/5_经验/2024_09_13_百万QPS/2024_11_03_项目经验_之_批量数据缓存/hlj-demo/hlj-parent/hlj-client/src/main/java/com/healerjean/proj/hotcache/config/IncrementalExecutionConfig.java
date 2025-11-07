package com.healerjean.proj.hotcache.config;


import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 增量快照执行配置
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Data
public class IncrementalExecutionConfig {

    /**
     * 数据集名称
     */
    private String datasetName;

    /**
     * 运行时增量配置
     */
    private DatasetIncrementalConfig incrementalConfig;


    /**
     * 计算下一个周期的开始时间（格式：yyyyMMddHHmmss），并支持偏移
     *
     * @param dateTime 当前时间
     * @param offset   偏移量（-1=前一个周期，0=当前周期，1=下一个周期）
     */
    public long getIncrIntervalTimeSecond(LocalDateTime dateTime, int offset) {
        int cycleMinutes = incrementalConfig.getTime2OffsetInterval() / 60;
        if (cycleMinutes % 5 != 0 && cycleMinutes != 1) {
            throw new IllegalArgumentException("cycleMinutes 必须是1或5的倍数");
        }
        LocalDateTime cleanMinute = dateTime.withSecond(0).withNano(0);
        int remainder = cleanMinute.getMinute() % cycleMinutes;
        LocalDateTime cycleStart = dateTime.minusMinutes(remainder);
        // 计算目标周期（当前周期 + offset）
        LocalDateTime targetCycleStart = cycleStart.plusMinutes((long) offset * cycleMinutes).withSecond(0).withNano(0);
        // 格式化为 yyyyMMddHHmm 字符串，再转为长整型（避免字符串操作，直接数字存储）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateStr = targetCycleStart.format(formatter);
        return Long.parseLong(dateStr);
    }

}