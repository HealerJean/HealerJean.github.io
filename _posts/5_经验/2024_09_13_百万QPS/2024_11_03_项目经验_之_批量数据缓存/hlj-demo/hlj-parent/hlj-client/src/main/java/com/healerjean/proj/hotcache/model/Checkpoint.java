package com.healerjean.proj.hotcache.model;

import lombok.Data;

/**
 * 快照任务断点状态，持久化以支持中断后恢复。
 *
 * 存储内容包括：
 * - 最后处理的主键
 * - 已成功写入条数
 * - 更新时间戳
 *
 * 文件存储路径格式：checkpoints/{datasetName}_v{version}.cp
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Data
public class Checkpoint {

    /**
     * 快照版本号（通常为时间戳）
     */
    private String version;

    /**
     * 上一次已处理的最后一条记录主键（字符串形式）
     */
    private String lastKey;

    /**
     * 已成功分布并写入的记录总数
     */
    private int processedCount;

    /**
     * 断点最后更新时间（毫秒时间戳）
     */
    private long updateTime;

    /**
     * 构造函数
     */
    public Checkpoint() {}

    public Checkpoint(String version, String lastKey, int processedCount) {
        this.version = version;
        this.lastKey = lastKey;
        this.processedCount = processedCount;
        this.updateTime = System.currentTimeMillis();
    }
}