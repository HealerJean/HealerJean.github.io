package com.healerjean.proj.hotcache.service.checkpoint;


import com.healerjean.proj.hotcache.config.SnapshotExecutionConfig;
import com.healerjean.proj.hotcache.constants.SnapshotPaths;
import com.healerjean.proj.hotcache.enums.SnapshotPathEnum;
import com.healerjean.proj.hotcache.model.Checkpoint;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 断点续传控制器，持久化任务进度以便中断后恢复。
 * 存储 lastKey 和已写入条数，防止重复处理。
 */
@Slf4j
@Component
public class CheckpointManager {

    /**
     * 加载指定数据源和版本的检查点信息。
     */
    public Checkpoint load(SnapshotExecutionConfig runConfig) {
        String datasetName = runConfig.getDatasetName();
        String version = runConfig.getVersion();
        StorageServiceStrategy storage = runConfig.getStorageServiceStrategy();
        String path = SnapshotPathEnum.FILE_CHECKPOINT_FILE_FORMAT.format(datasetName, version, datasetName, version);
        try {
            if (!storage.exists(path)) {
                log.info("检查点文件不存在: {}", path);
                return null;
            }
        } catch (Exception e) {
            log.warn("读取 checkpoint 失败: 路径={}, 错误={}", path, e.getMessage());
            return null;
        }

        try (java.io.InputStreamReader reader = new java.io.InputStreamReader(storage.download(path))) {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            return gson.fromJson(reader, Checkpoint.class);
        } catch (Exception e) {
            log.warn("读取 checkpoint 失败: 路径={}, 错误={}", path, e.getMessage());
            return null;
        }
    }


    /**
     * 保存当前处理进度。
     *
     * @param lastKey     已处理到最后一条记录的键。
     * @param totalCount  已成功写入的总记录数。
     */
    public void save(SnapshotExecutionConfig runConfig, Object lastKey, int totalCount) throws java.io.IOException {
        String datasetName = runConfig.getDatasetName();
        String version = runConfig.getVersion();
        StorageServiceStrategy storage = runConfig.getStorageServiceStrategy();
        Checkpoint cp = new Checkpoint();
        cp.setVersion(version);
        cp.setLastKey(String.valueOf(lastKey));
        cp.setProcessedCount(totalCount);
        cp.setUpdateTime(System.currentTimeMillis());

        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = gson.toJson(cp);
        String path = SnapshotPathEnum.FILE_CHECKPOINT_FILE_FORMAT.format(datasetName, version, datasetName, version);
        try (java.io.OutputStream os = storage.getOutputStream(path)) {
            os.write(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
        storage.publish(path + SnapshotPaths.TEMP_FILE_SUFFIX, path);

        log.debug("保存检查点: dataset={}, version={}, processed={}", datasetName, version, totalCount);
    }

}