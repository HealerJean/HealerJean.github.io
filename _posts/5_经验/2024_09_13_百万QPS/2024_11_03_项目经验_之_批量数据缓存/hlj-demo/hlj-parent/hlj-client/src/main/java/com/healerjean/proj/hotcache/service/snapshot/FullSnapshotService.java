package com.healerjean.proj.hotcache.service.snapshot;

import com.healerjean.proj.hotcache.config.SnapshotExecutionConfig;
import com.healerjean.proj.hotcache.config.SnapshotGlobalConfig;
import com.healerjean.proj.hotcache.service.clearup.SnapshotCleanupService;
import com.healerjean.proj.hotcache.service.manifest.ManifestGenerator;
import com.healerjean.proj.hotcache.service.publish.SnapshotPublisher;
import com.healerjean.proj.hotcache.shard.ShardWriterCoordinator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 全量快照生成主流程控制器。
 */
@Slf4j
@Component
public class FullSnapshotService {

    @Autowired
    private ManifestGenerator manifestGenerator;
    @Autowired
    private SnapshotPublisher snapshotPublisher;
    @Autowired
    private SnapshotCleanupService cleanupService;
    @Autowired
    private SnapshotGlobalConfig snapshotGlobalConfig;
    @Autowired
    private BatchDataLoader batchDataLoader;

    public String generate(String datasetName, LocalDateTime dateTime) throws Exception {

        // 根据数据集名称获取运行配置
        long start = System.currentTimeMillis();
        SnapshotExecutionConfig runConfig = snapshotGlobalConfig.instanceRunConfig(datasetName, dateTime);
        String version = runConfig.getVersion();
        log.info("🚀 开始生成快照: dataset={}, version={}", datasetName, version);

        try {
            // 2. 创建分片管理器
            ShardWriterCoordinator shardWriterCoordinator = new ShardWriterCoordinator<>(runConfig);

            // 3. 加载并分发数据
            int total = batchDataLoader.loadDataAndDistribute(shardWriterCoordinator, runConfig);

            // 4. 并行关闭所有分片
            shardWriterCoordinator.closeAllInParallel();

            // 5. 生成清单
            manifestGenerator.generate(runConfig, shardWriterCoordinator.getWriters(), total);

            // 6. 发布到 Redis
            snapshotPublisher.publish(runConfig);

            // 7. 清理旧版本
            cleanupService.cleanupOld(runConfig);

            log.info("✅ 快照生成完成: dataset={}, version={}, records={}, duration={}ms",
                    datasetName, version, total, System.currentTimeMillis() - start);

        } catch (Exception e) {
            log.error("❌ 快照生成失败: dataset={}, error={}", datasetName, e.getMessage(), e);
            throw e;
        }

        return version;
    }


}