package com.healerjean.proj.hotcache.model;

import lombok.Data;

import java.util.List;

/**
 * 汇总结果
 *
 * @author zhangyujin
 * @date 2025/11/4
 */
@Data
public class DiffTotalResult {
    public int added = 0;
    public int deleted = 0;
    public int modified = 0;
    public int sourceTotalCount = 0;
    public int targetTotalCount = 0;

    /**
     * 分片结果
     */
    public List<DiffShardResult> shardDiffResults;



    /**
     * 获取总变更数
     */
    public int getTotalChanges() {
        return added + deleted + modified;
    }

    /**
     * 获取变更比例
     */
    public double getChangeRatio() {
        int totalRecords = Math.max(sourceTotalCount, targetTotalCount);
        return totalRecords > 0 ? (double) getTotalChanges() / totalRecords : 0.0;
    }

    /**
     * 获取总记录数
     */
    public int getTotalRecords() {
        return Math.max(sourceTotalCount, targetTotalCount);
    }
}
