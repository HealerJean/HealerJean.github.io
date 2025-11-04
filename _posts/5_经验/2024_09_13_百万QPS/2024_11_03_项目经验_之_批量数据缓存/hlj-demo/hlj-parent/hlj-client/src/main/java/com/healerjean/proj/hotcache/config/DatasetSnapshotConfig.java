package com.healerjean.proj.hotcache.config;

import lombok.Data;

/**
 * 单个数据集的快照生成配置项。
 * 可在运行时由配置中心动态刷新。
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Data
public class DatasetSnapshotConfig {

    /**
     * 分片数量，默认 8。
     */
    private Integer shardCount = 8;

    /**
     * 每批次加载记录数，默认 1000。
     */
    private Integer batchSize = 1000;

    /**
     * 是否启用 GZIP 压缩输出文件。
     */
    private Boolean compress = true;

    /**
     * 序列化策略：支持 fastjson、protobuf。
     */
    private String serializerStrategy = "fastjson";

    /**
     * 使用的线程池名称，需在 ThreadPoolFactory 中注册。
     */
    private String threadPool = "default-pool";

    /**
     * 写入并发度，默认 10。
     */
    private Integer maxConcurrent = 1000;

    /**
     * 是否启用断点续传功能
     */
    private Boolean enableCheckpoint = true;

    /**
     * 存储服务
     */
    private String storageStrategy = "local";

}

