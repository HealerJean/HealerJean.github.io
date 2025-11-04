package com.healerjean.proj.hotcache.config;

import com.healerjean.proj.hotcache.factory.SnapshotFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
     * 默认配置模板，用于未显式指定的数据集。
     */
    private DatasetSnapshotConfig defaultConfig = new DatasetSnapshotConfig();
    /**
     * 各个数据集的个性化配置映射。
     * Key: datasetName (e.g., "user-tag")
     */
    private Map<String, DatasetSnapshotConfig> datasets;

    /**
     * 根据数据集名称获取最终生效配置（优先级：datasets > default）
     */
    public SnapshotExecutionConfig instanceRunConfig(String datasetName, String version) {
        SnapshotExecutionConfig runConfig = new SnapshotExecutionConfig();
        runConfig.setDatasetName(datasetName);
        runConfig.setVersion(version);

        // 获取配置：优先使用datasets中的配置，否则使用默认配置
        DatasetSnapshotConfig config = datasets != null ? datasets.get(datasetName) : null;
        if (config == null) {
            config = defaultConfig;
            log.info("使用默认配置生成数据集: {}", datasetName);
        } else {
            log.info("使用个性化配置生成数据集: {}", datasetName);
        }
        runConfig.setDatasetSnapshotConfig(config);
        SnapshotFactory snapshotFactory = SnapshotFactory.getInstance();
        runConfig.setDataSerializerStrategy(snapshotFactory.getDataSerializer(datasetName, config.getSerializerStrategy()));
        runConfig.setSnapshotDataSource(snapshotFactory.getSnapshotDataSource(datasetName));
        runConfig.setStorageServiceStrategy(snapshotFactory.getStorageServiceStrategy(datasetName, config.getStorageStrategy()));
        return runConfig;
    }

}


