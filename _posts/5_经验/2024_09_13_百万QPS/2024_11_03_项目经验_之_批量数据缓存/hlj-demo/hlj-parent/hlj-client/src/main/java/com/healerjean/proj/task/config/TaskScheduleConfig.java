package com.healerjean.proj.task.config;

/**
 * TaskScheduleConfig
 *
 * @author zhangyujin
 * @date 2025/11/11
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "task.schedule")
public class TaskScheduleConfig {

    /**
     * 是否启用定时任务
     */
    private boolean enabled = true;

    /**
     * 执行间隔，单位：秒（默认 3600 秒 = 1 小时）
     */
    private long intervalSeconds = 3600;

    /**
     * 总批次数（默认 10 批）
     */
    private int totalBatches = 10;


}