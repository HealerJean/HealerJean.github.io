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
    REDIS_INCR_LATEST_OFFSET_KEY("incr:'{'{0}'}':latestOffset", "增量已连续落地的最大偏移量（读方可见）[dataset]"),
    REDIS_INCR_ASSIGNED_OFFSET_KEY("incr:'{'{0}'}':assignedOffset", "增量已分配偏移量（写侧内部）[dataset]"),
    REDIS_INCR_ADVANCE_LOCK_KEY("incr:'{'{0}'}':advanceLock", "推进 latestOffset 的互斥锁 [dataset]"),
    REDIS_INCR_CURRENT_SHARD_KEY("incr:'{'{0}'}':shard:{1}", "增量分片数据 [dataset, shardId]，hash tag 保证同 slot（Lua 版本使用）"),
    REDIS_INCR_TIME_OFFSET_KEY("incr:'{'{0}'}':timeOffset:{1}", "时间窗口对应最新偏移量 [dataset, yyyyMMddHHmm]，hash tag 保证同 slot"),
    REDIS_INCR_DATA_KEY("incr:{0}:data:{1}", "增量数据本体 [dataset, offset]，无 hash tag，offset 决定 slot 便于均匀分布"),
    REDIS_INCR_DATA_INDEX_KEY("incr:{0}:dataIndex:{1}", "data → offset 反向索引 [dataset, dataId]，ZSET 记录变更历史，仅排查用"),
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
