package com.healerjean.proj.hotcache.config;


import lombok.Data;

/**
 * 增量快照配置项
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Data
public class DatasetIncrementalConfig {

    /**
     * zset最大成员数
     * 1、在项目上线前确，并固化。不要在运行时修改它（除非你有完整的数据迁移方案）。
     * 2、如果未来真要改，必须：停写，遍历所有旧 shard，按新规则 re-shard 数据，更新 offset 记录 再启写
     */
    private Integer maxMembersSize = 10;

    /**
     * 数据库存储偏移量周期
     */
    private Integer dbSaveOffsetInterval = 5;

    /**
     * 数据过期时间，单位秒
     */
    private Long ttlSeconds;

    /**
     * 默认 每5分钟记录一次时间对应的偏移量（必须是60的倍数，分钟级）
     */
    private Integer time2OffsetInterval = 60;


}