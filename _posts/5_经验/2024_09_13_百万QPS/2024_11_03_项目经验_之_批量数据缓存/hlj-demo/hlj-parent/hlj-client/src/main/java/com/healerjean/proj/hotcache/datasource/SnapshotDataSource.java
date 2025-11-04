package com.healerjean.proj.hotcache.datasource;

import com.healerjean.proj.hotcache.enums.DatesetKeyEnum;

import java.util.List;

/**
 * 快照数据提供者接口，定义了所有可生成快照的数据实体必须实现的方法。
 * 避免与 javax.sql.DataSource 冲突。
 *
 * @param <T> 数据实体的泛型类型，例如 UserTag, ItemProfile。
 * @author zhangyujin
 * @date 2025-11-04 04:11:21
 */
public interface SnapshotDataSource<T> {

    /**
     * 数据集名称
     */
    DatesetKeyEnum getDatesetKeyEnum();

    /**
     * 分页加载一批数据记录。
     */
    List<T> loadBatch(Object lastKey);

    /**
     * 从数据实体中提取用于分片的业务主键。
     */
    Object extractKey(T record);

    /**
     * 预估当前数据源的总记录数，用于最终完整性校验。
     */
    long estimateTotalCount();
}