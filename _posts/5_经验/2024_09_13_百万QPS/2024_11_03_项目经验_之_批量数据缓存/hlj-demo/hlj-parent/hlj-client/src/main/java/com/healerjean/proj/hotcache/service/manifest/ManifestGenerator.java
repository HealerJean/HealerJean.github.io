package com.healerjean.proj.hotcache.service.manifest;


import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.healerjean.proj.hotcache.config.DatasetSnapshotConfig;
import com.healerjean.proj.hotcache.config.SnapshotExecutionConfig;
import com.healerjean.proj.hotcache.constants.SnapshotPaths;
import com.healerjean.proj.hotcache.enums.SnapshotPathEnum;
import com.healerjean.proj.hotcache.model.SnapshotMetadata;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import com.healerjean.proj.hotcache.shard.ShardWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

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
     * @param runConfig        配置信息
     * @param writers          所有分片写入器列表。
     * @param recordTotalCount 预期的总记录数（从数据源预估或加载得到）。
     * @throws IOException 生成或写入失败时抛出。
     */
    public void generate(SnapshotExecutionConfig runConfig, List<ShardWriter<?>> writers, int recordTotalCount) throws IOException {

        String datasetName = runConfig.getDatasetName();
        String version = runConfig.getVersion();
        DatasetSnapshotConfig config = runConfig.getDatasetSnapshotConfig();
        StorageServiceStrategy storageServiceStrategy = runConfig.getStorageServiceStrategy();
        SnapshotMetadata metadata = new SnapshotMetadata();
        metadata.setDatasetName(datasetName);
        metadata.setVersion(version);
        metadata.setExecuteTime(runConfig.getExecuteTime());
        metadata.setIncrPullTimeLowerBound(runConfig.getIncrPullTimeLowerBound());
        metadata.setRecordTotalCount(recordTotalCount);
        metadata.setConfig(config);
        Map<Integer, SnapshotMetadata.FileInfo> fileInfoMap = Maps.newHashMap();
        metadata.setFileInfoMap(fileInfoMap);
        int actualTotal = 0;

        for (ShardWriter<?> w : writers) {
            String filename = w.getFilename();

            SnapshotMetadata.FileInfo fi = new SnapshotMetadata.FileInfo();
            fi.setFilename(filename);
            fi.setRecordCount(w.getRecordCount());
            fi.setFileSize(storageServiceStrategy.getFileSize(filename));
            fi.setMd5(storageServiceStrategy.getFileMd5(filename));
            fileInfoMap.put(w.getShardId(), fi);
            actualTotal += fi.getRecordCount();
        }
        if (actualTotal != recordTotalCount) {
            throw new RuntimeException(String.format("【数据完整性校验失败】记录总数不一致！数据源=%s, 版本=%s, 预期=%d, 实际=%d", datasetName, version, recordTotalCount, actualTotal));
        }
        String json = GSON.toJson(metadata);
        String manifestPath =   SnapshotPathEnum.FILE_MANIFEST_FILE_FORMAT.format(datasetName, version, datasetName, version);
        metadata.setManifestUrl(manifestPath);
        try (OutputStream os = storageServiceStrategy.getOutputStream(manifestPath)) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        runConfig.setSnapshotMetadata(metadata);
        storageServiceStrategy.publish(manifestPath + SnapshotPaths.TEMP_FILE_SUFFIX, manifestPath);
        log.info("✅ Manifest 生成完成: {}, 总条数: {}", manifestPath, actualTotal);
    }
}
