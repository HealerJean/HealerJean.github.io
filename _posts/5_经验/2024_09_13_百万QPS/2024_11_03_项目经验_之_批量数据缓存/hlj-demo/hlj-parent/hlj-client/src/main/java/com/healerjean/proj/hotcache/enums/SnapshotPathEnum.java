package com.healerjean.proj.hotcache.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.MessageFormat;

/**
 * SnapshotPath
 *
 * @author zhangyujin
 * @date 2025/11/6
 */
@AllArgsConstructor
@Getter
public enum SnapshotPathEnum {


    FILE_SNAPSHOT_FILE_FORMAT("{0}/{1}/snapshots/{2}_v{3}_shard_{4}", "快照分片文件路径格式  [dataset, yyyyMMdd, version, dataset, version, shardId]"),
    FILE_CHECKPOINT_FILE_FORMAT("{0}/{1}/checkpoints/{2}_v{3}.cp","检查点文件路径格式 [dataset, yyyyMMdd, version, dataset, version]"),
    FILE_MANIFEST_FILE_FORMAT("{0}/{1}/manifest/{2}_v{3}_manifest.json","清单文件路径格式 [dataset, yyyyMMdd, version, dataset, version]"),

    REDIS_SNAPSHOT_VERSIONS_KEY("{0}:snapshot:versions", "最近的版本集合 [dataset]"),
    REDIS_SNAPSHOT_LATEST_VERSION_KEY("{0}:snapshot:latestVersion", "最新快照版本 [dataset]"),
    REDIS_SNAPSHOT_VERSION_META_KEY("{0}:snapshot:versionMeta:{1}", "版本元数据 [dataset, version]"),
    REDIS_INCR_LATEST_OFFSET_KEY("{0}:incr:latestOffset", "增量最新偏移量 [dataset]"),
    REDIS_INCR_CURRENT_SHARD_KEY("{0}:incr:shard:{1}", "增量分片数据 [dataset, shardId]"),
    REDIS_INCR_TIME_PERIOD_KEY("{0}:incr:timePeriod", "时间区间 [dataset]"),
    REDIS_INCR_TIME_OFFSET_KEY("{0}:incr:timeOffset:{1}", "时间对应偏移量 [dataset, time]"
    ),
    ;


    private final String pattern;

    private final String desc;

    /**
     * 使用给定参数格式化模板
     *
     * @param args 按顺序填充 {} 占位符的参数
     * @return 格式化后的字符串
     */
    public String format(Object... args) {
        return MessageFormat.format(pattern, args);
    }

}
