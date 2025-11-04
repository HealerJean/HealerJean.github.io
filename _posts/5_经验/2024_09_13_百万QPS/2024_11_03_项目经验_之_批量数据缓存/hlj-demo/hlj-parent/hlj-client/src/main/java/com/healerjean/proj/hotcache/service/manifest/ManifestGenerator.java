package com.healerjean.proj.hotcache.service.manifest;


import com.google.gson.Gson;
import com.healerjean.proj.hotcache.config.DatasetSnapshotConfig;
import com.healerjean.proj.hotcache.config.SnapshotExecutionConfig;
import com.healerjean.proj.hotcache.constants.SnapshotPaths;
import com.healerjean.proj.hotcache.model.SnapshotFileMetadata;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import com.healerjean.proj.hotcache.shard.ShardWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成快照的清单文件（manifest.json），包含所有分片文件的元信息和校验和。
 * 这是数据完整性校验的核心。
 */
@Slf4j
@Component
public class ManifestGenerator {
    private static final Gson GSON = new Gson();

    /**
     * 生成清单文件。
     * 此方法会执行关键的 **数据完整性校验**：
     * 1. 检查实际写入的总记录数是否等于预期总数。
     * 2. 为每个文件计算 MD5 和大小。
     *
     * @param runConfig 配置信息
     * @param writers 所有分片写入器列表。
     * @param recordTotalCount 预期的总记录数（从数据源预估或加载得到）。
     * @throws IOException 生成或写入失败时抛出。
     */
    public void generate(SnapshotExecutionConfig runConfig, List<ShardWriter<?>> writers, int recordTotalCount) throws IOException {

        String datasetName = runConfig.getDatasetName();
        String version = runConfig.getVersion();
        DatasetSnapshotConfig config = runConfig.getDatasetSnapshotConfig();
        StorageServiceStrategy storageServiceStrategy = runConfig.getStorageServiceStrategy();
        SnapshotFileMetadata metadata = new SnapshotFileMetadata();
        metadata.setDatasetName(datasetName);
        metadata.setVersion(version);
        metadata.setCreateTime(System.currentTimeMillis());
        metadata.setRecordTotalCount(recordTotalCount);
        metadata.setConfig(config);

        List<SnapshotFileMetadata.FileInfo> files = new ArrayList<>();
        int actualTotal = 0;

        for (ShardWriter<?> w : writers) {
            String filename = w.getFilename();

            SnapshotFileMetadata.FileInfo fi = new SnapshotFileMetadata.FileInfo();
            fi.setFilename(filename);
            fi.setRecordCount(w.getRecordCount());
            fi.setFileSize(storageServiceStrategy.getFileSize(filename));
            fi.setMd5(storageServiceStrategy.getFileMd5(filename));

            files.add(fi);
            actualTotal += fi.getRecordCount();
        }

        metadata.setFiles(files);

        // **核心校验：数据完整性**
        if (actualTotal != recordTotalCount) {
            throw new RuntimeException(
                    String.format("【数据完整性校验失败】记录总数不一致！数据源=%s, 版本=%s, 预期=%d, 实际=%d",
                            datasetName, version, recordTotalCount, actualTotal)
            );
        }

        String json = GSON.toJson(metadata);
        String manifestPath = String.format(SnapshotPaths.MANIFEST_FILE_FORMAT, datasetName, datasetName, version);
        try (OutputStream os = storageServiceStrategy.getOutputStream(manifestPath)) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        storageServiceStrategy.publish(manifestPath + SnapshotPaths.TEMP_FILE_SUFFIX, manifestPath);

        log.info("✅ Manifest 生成完成: {}, 总条数: {}", manifestPath, actualTotal);
    }
}
