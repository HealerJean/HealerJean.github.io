package com.healerjean.proj.hotcache.service.clearup;

import com.healerjean.proj.hotcache.config.SnapshotExecutionConfig;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 定期清理过期快照文件和检查点，释放存储空间。
 */
@Component
public class SnapshotCleanupService {

    private static final Pattern VERSION_PATTERN = Pattern.compile("_v(\\d+)");

    /**
     * 清理早于 currentVersion 的历史快照。
     */
    public void cleanupOld(SnapshotExecutionConfig runConfig) {
        try {
            long currentVersion = Long.parseLong(runConfig.getVersion()) ;
            String datasetName = runConfig.getDatasetName();
            StorageServiceStrategy storage = runConfig.getStorageServiceStrategy();
            // 清理检查点文件
            String checkpointPrefix = String.format("%s/checkpoints/%s_v", datasetName, datasetName);
            for (String file : storage.listFiles(checkpointPrefix)) {
                Long ver = extractVersion(file);
                if (ver != null && ver < currentVersion) {
                    storage.delete(file);
                    System.out.println("🗑️ 删除旧检查点文件: " + file);
                }
            }

            // 清理清单文件
            String manifestPrefix = String.format("%s/manifest/%s_v", datasetName, datasetName);
            for (String file : storage.listFiles(manifestPrefix)) {
                Long ver = extractVersion(file);
                if (ver != null && ver < currentVersion) {
                    storage.delete(file);
                    System.out.println("🗑️ 删除旧清单文件: " + file);
                }
            }

            // 清理快照分片文件
            String snapshotPrefix = String.format("%s/snapshots/%s_v", datasetName, datasetName);
            for (String file : storage.listFiles(snapshotPrefix)) {
                Long ver = extractVersion(file);
                if (ver != null && ver < currentVersion) {
                    storage.delete(file);
                    System.out.println("🗑️ 删除旧快照分片: " + file);
                }
            }

        } catch (IOException e) {
            System.err.println("清理失败: " + e.getMessage());
        }
    }

    private Long extractVersion(String filename) {
        Matcher m = VERSION_PATTERN.matcher(filename);
        return m.find() ? Long.parseLong(m.group(1)) : null;
    }
}