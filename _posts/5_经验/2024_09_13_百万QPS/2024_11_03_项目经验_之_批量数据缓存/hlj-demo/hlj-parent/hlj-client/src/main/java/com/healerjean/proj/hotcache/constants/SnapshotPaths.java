package com.healerjean.proj.hotcache.constants;

/**
 * PathConstants
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
public class SnapshotPaths {

    // 快照分片文件路径格式
    public static final String SNAPSHOT_FILE_FORMAT = "%s/snapshots/%s_v%s_part_%03d";

    // 检查点文件路径格式
    public static final String CHECKPOINT_FILE_FORMAT = "%s/checkpoints/%s_v%s.cp";

    // 清单文件路径格式
    public static final String MANIFEST_FILE_FORMAT = "%s/manifest/%s_v%s_manifest.json";

    // 临时文件后缀
    public static final String TEMP_FILE_SUFFIX = ".tmp";

    // Redis键格式
    public static final String REDIS_LATEST_KEY_FORMAT = "snapshot:%s:latest";
    public static final String REDIS_INFO_KEY_FORMAT = "snapshot:%s:%s";

    // 最近的几个redis版本（保留最近的10个版本）
    public static final String REDIS_VERSION_KEY_FORMAT = "snapshot:%s:versions";


    // 存储服务根目录
    public static final String STORAGE_ROOT = "snapshots";

    // 检查点存储目录
    public static final String CHECKPOINT_ROOT = "checkpoints";

    // 清单存储目录
    public static final String MANIFEST_ROOT = "manifest";

    // 临时文件目录
    public static final String TEMP_ROOT = "temp";
}
