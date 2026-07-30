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
     * 计算时间对齐到当前周期起始时间（格式：yyyyMMddHHmm）
     * 按配置的分钟粒度向下对齐到整点，例如：
     * - timeIntervalMinutes=1 → 10:17:33 → "202607281017"
     * - timeIntervalMinutes=5 → 10:17:33 → "202607281015"
     *
     * @param dateTime 当前时间
     * @return 当前周期起始时间标识，如 "202607281017"
     */
    public String getIncrIntervalMinuteKey(LocalDateTime dateTime) {
        int intervalMinutes = incrementalConfig.getTimeIntervalMinutes();
        LocalDateTime cleanMinute = dateTime.withSecond(0).withNano(0);
        int remainder = cleanMinute.getMinute() % intervalMinutes;
        LocalDateTime cycleStart = cleanMinute.minusMinutes(remainder);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return cycleStart.format(formatter);
    }

}