package com.healerjean.proj.hotcache.model;


import com.healerjean.proj.hotcache.config.DatasetSnapshotConfig;
import lombok.Data;

/**
 * Redis中存储的快照元数据对象
 */
@Data
public class SnapshotRedisMetadata {
    /**
     * 数据源名称
     */
    private String dataSource;

    /**
     * 快照版本
     */
    private String version;

    /**
     * 创建时间戳
     */
    private long createTime;

    /**
     * 记录总数
     */
    private int recordCount;

    /**
     * 清单文件URL
     */
    private String manifestUrl;

    /**
     * 配置信息
     */
    private DatasetSnapshotConfig config;
}
