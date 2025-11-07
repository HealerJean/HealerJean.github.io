package com.healerjean.proj.hotcache.config;

import com.healerjean.proj.hotcache.datasource.SnapshotDataSource;
import com.healerjean.proj.hotcache.model.SnapshotMetadata;
import com.healerjean.proj.hotcache.service.serialization.DataSerializerStrategy;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import lombok.Data;

/**
 * SnapshotConfig
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Data
public class SnapshotExecutionConfig {

    /**
     * 快照版本
     */
    private String version;

    /**
     * 数据集名称
     */
    private String datasetName;

    /**
     * 任务执行时间
     */
    private String executeTime;

    /**
     * 增量拉取时间的下界（分钟级） yyyyMMddHHmm
     */
    private Long incrPullTimeLowerBound;

    /**
     * 运行时内存配置，可通过配置中心进行变更
     */
    private DatasetSnapshotConfig datasetSnapshotConfig;

    /**
     * 序列号器
     */
    private DataSerializerStrategy<?> dataSerializerStrategy;

    /**
     * 存储服务
     */
    private StorageServiceStrategy storageServiceStrategy;

    /**
     * 数据提供服务
     */
    private SnapshotDataSource<?> snapshotDataSource;

    /**
     * 快照元数据
     */
    private SnapshotMetadata snapshotMetadata;



}
