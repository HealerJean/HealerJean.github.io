package com.healerjean.proj.hotcache.service.snapshot;

import com.healerjean.proj.hotcache.model.Checkpoint;
import com.healerjean.proj.hotcache.service.checkpoint.CheckpointManager;
import com.healerjean.proj.hotcache.config.SnapshotExecutionConfig;
import com.healerjean.proj.hotcache.config.DatasetSnapshotConfig;
import com.healerjean.proj.hotcache.factory.ThreadPoolFactory;
import com.healerjean.proj.hotcache.datasource.SnapshotDataSource;
import com.healerjean.proj.hotcache.shard.ShardWriterCoordinator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

/**
 * 负责从数据源加载数据，并分发到分片管理器。
 *
 * @author zhangyujin
 * @date 2025-11-04 03:11:16
 */
@Slf4j
@Component
public class BatchDataLoader<T> {


    /**
     * checkpointManager
     */
    @Resource
    private CheckpointManager checkpointManager;

    public int loadDataAndDistribute(ShardWriterCoordinator<T> writerManager, SnapshotExecutionConfig runConfig) throws Exception {
        // 1、获取配置基本数据
        String datasetName = runConfig.getDatasetName();
        DatasetSnapshotConfig config = runConfig.getDatasetSnapshotConfig();
        int batchSize = config.getBatchSize();
        boolean enableCheckpoint = config.getEnableCheckpoint();
        SnapshotDataSource<T> provider = (SnapshotDataSource<T>)runConfig.getSnapshotDataSource();
        int total = 0;
        Object lastKey = null;

        // 2、按照版本恢复断点
        if (enableCheckpoint) {
            Checkpoint cp = checkpointManager.load(runConfig);
            if (cp != null) {
                lastKey = cp.getLastKey();
                total = cp.getProcessedCount();
                log.info("恢复断点: dataset={}, lastKey={}, processed={}", datasetName, lastKey, total);
            }
        }

        // 3、线程池等相关配置
        ExecutorService dataLoadExecutor = ThreadPoolFactory.getThreadPool(config.getThreadPool());
        Semaphore semaphore = new Semaphore(config.getMaxConcurrent());

        // 3、进行数据加载
        try {
            while (true) {
                // a、加载一批数据
                List<T> batch = provider.loadBatch(lastKey);
                if (batch.isEmpty()) {
                    break;
                }

                // b、倒计时器，判断线程池完成
                CountDownLatch latch = new CountDownLatch(batch.size());

                // c、线程池任务执行与限流控制
                for (T record : batch) {
                    Object key = provider.extractKey(record);
                    semaphore.acquire();
                    dataLoadExecutor.submit(() -> {
                        try {
                            writerManager.distribute(record, key);
                        } catch (IOException e) {
                            log.error("Failed to write record to shard: {}", e.getMessage(), e);
                            throw new RuntimeException("Failed to write record to shard", e);
                        } finally {
                            // 释放许可
                            semaphore.release();
                            latch.countDown();
                        }
                    });
                }

                // d、等待当前批次全部完成（确保 total/lastKey 更新前数据已写完）
                latch.await();

                // e、每处理 100 个批次（即 100 × batchSize 条记录）保存一次。
                total += batch.size();
                lastKey = provider.extractKey(batch.get(batch.size() - 1));
                if (enableCheckpoint && total % (batchSize * 100) == 0) {
                    checkpointManager.save(runConfig, lastKey, total);
                    log.info("保存断点: dataset={}, processed={}", datasetName, total);
                }
            }
        } finally {
            // 无论成功与否，都尝试保存最终断点
            if (enableCheckpoint && lastKey != null) {
                checkpointManager.save(runConfig, lastKey, total);
                log.info("保存最终断点: dataset={}, processed={}", datasetName, total);
            }
        }

        return total;
    }
}