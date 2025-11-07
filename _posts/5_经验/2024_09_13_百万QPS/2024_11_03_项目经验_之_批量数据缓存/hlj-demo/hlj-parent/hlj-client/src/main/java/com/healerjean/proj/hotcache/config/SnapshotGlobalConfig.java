package com.healerjean.proj.hotcache.config;

import com.healerjean.proj.hotcache.factory.SnapshotFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 内存全局配置文件
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "hot-snapshot")
public class SnapshotGlobalConfig {

    /**
     * 各个数据集的个性化配置映射。
     * Key: datasetName (e.g., "user_tag")
     */
    private Map<String, DatasetConfig> datasets;


    @Data
    public static class DatasetConfig {
        /**
         * 全量快照配置
         */
        private DatasetSnapshotConfig snapshot = new DatasetSnapshotConfig();

        /**
         * 增量快照配置
         */
        private DatasetIncrementalConfig incremental = new DatasetIncrementalConfig();
    }


    /**
     * 根据数据集名称获取全量快照最终生效配置
     */
    public SnapshotExecutionConfig instanceRunConfig(String datasetName, LocalDateTime dateTime) {
        SnapshotExecutionConfig runConfig = new SnapshotExecutionConfig();
        runConfig.setDatasetName(datasetName);
        runConfig.setVersion(dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        runConfig.setExecuteTime(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // 获取配置：从 datasets -> datasetName -> snapshot
        DatasetConfig datasetConfig = datasets.get(datasetName);
        DatasetSnapshotConfig snapshotConfig = datasetConfig != null ? datasetConfig.getSnapshot() : new DatasetSnapshotConfig();
        runConfig.setDatasetSnapshotConfig(snapshotConfig);

        IncrementalExecutionConfig incrementalExecutionConfig = instanceIncrementalConfig(datasetName);
        Integer incrPullInterval = snapshotConfig.getIncrPullInterval();
        long incrIntervalTimeSecond = incrementalExecutionConfig.getIncrIntervalTimeSecond(dateTime, incrPullInterval);
        runConfig.setIncrPullTimeLowerBound(incrIntervalTimeSecond);

        SnapshotFactory snapshotFactory = SnapshotFactory.getInstance();
        runConfig.setDataSerializerStrategy(snapshotFactory.getDataSerializer(datasetName, snapshotConfig.getSerializerStrategy()));
        runConfig.setSnapshotDataSource(snapshotFactory.getSnapshotDataSource(datasetName));
        runConfig.setStorageServiceStrategy(snapshotFactory.getStorageServiceStrategy(datasetName, snapshotConfig.getStorageStrategy()));
        return runConfig;
    }

    /**
     * 根据数据集名称获取增量快照最终生效配置
     */
    public IncrementalExecutionConfig instanceIncrementalConfig(String datasetName) {
        IncrementalExecutionConfig runConfig = new IncrementalExecutionConfig();
        runConfig.setDatasetName(datasetName);

        // 获取配置：从 datasets -> datasetName -> incremental
        DatasetConfig datasetConfig = datasets.get(datasetName);
        DatasetIncrementalConfig config = datasetConfig != null ? datasetConfig.getIncremental() : new DatasetIncrementalConfig();
        runConfig.setIncrementalConfig(config);
        return runConfig;
    }



}


