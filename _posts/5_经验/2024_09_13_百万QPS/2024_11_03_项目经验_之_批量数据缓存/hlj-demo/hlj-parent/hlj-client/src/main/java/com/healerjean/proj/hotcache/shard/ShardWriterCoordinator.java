package com.healerjean.proj.hotcache.shard;


import com.healerjean.proj.hotcache.config.DatasetSnapshotConfig;
import com.healerjean.proj.hotcache.config.SnapshotExecutionConfig;
import com.healerjean.proj.hotcache.constants.SnapshotPaths;
import com.healerjean.proj.hotcache.factory.ThreadPoolFactory;
import com.healerjean.proj.hotcache.service.serialization.DataSerializerStrategy;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * 分片写入器的统一管理器。
 * 负责创建、获取和批量关闭 ShardWriter。
 *
 * @author zhangyujin
 * @date 2025-11-04 09:11:53
 */
@Slf4j
public class ShardWriterCoordinator<T> {

    @Getter
    private final List<ShardWriter<T>> writers;
    private final int shardCount;
    private final ExecutorService writerPool;

    public ShardWriterCoordinator(SnapshotExecutionConfig runConfig) throws IOException {
        DatasetSnapshotConfig config = runConfig.getDatasetSnapshotConfig();
        this.shardCount = config.getShardCount();
        this.writerPool = ThreadPoolFactory.getThreadPool(config.getThreadPool());
        this.writers = createWriters(runConfig);
    }

    private List<ShardWriter<T>> createWriters(SnapshotExecutionConfig runConfig) throws IOException {
        List<ShardWriter<T>> ws = new ArrayList<>(shardCount);
        DatasetSnapshotConfig datasetSnapshotConfig = runConfig.getDatasetSnapshotConfig();
        boolean compress = datasetSnapshotConfig.getCompress();
        String datasetName = runConfig.getDatasetName();
        String version = runConfig.getVersion();
        DataSerializerStrategy<T> serializer = (DataSerializerStrategy<T>)runConfig.getDataSerializerStrategy();
        StorageServiceStrategy storage = runConfig.getStorageServiceStrategy();

        for (int i = 0; i < shardCount; i++) {
            String filename = String.format(SnapshotPaths.SNAPSHOT_FILE_FORMAT, datasetName, datasetName, version, i);
            ws.add(new ShardWriter<>(i, filename, storage, serializer, compress));
        }
        return ws;
    }

    /**
     * 将一条记录分发到对应的分片。
     */
    public void distribute(T record, Object key) throws IOException {
        int shardId = Math.abs(key.hashCode()) % shardCount;
        writers.get(shardId).add(record);
    }

    /**
     * 并行关闭所有写入器。
     */
    public void closeAllInParallel() {
        CompletableFuture.allOf(writers.stream()
                .map(writer -> CompletableFuture.runAsync(() -> {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        log.error("关闭 ShardWriter 失败: {}", e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }, writerPool)).toArray(CompletableFuture[]::new)).join();
    }

}
