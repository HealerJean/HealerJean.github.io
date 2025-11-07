package com.healerjean.proj.hotcache.model;



/**
 * 快照元数据对象，用于在Redis和清单文件中存储统一的元数据信息
 *
 * @author zhangyujin
 * @date 2025-11-04 09:11:19
 */

import com.healerjean.proj.hotcache.config.DatasetIncrementalConfig;
import lombok.Data;

@Data
public class IncrementMetadata {
    /**
     * 数据源名称
     */
    private String datasetName;

    /**
     * 最新便宜量
     */
    private Long latestOffset;

    /**
     * 配置信息
     */
    private DatasetIncrementalConfig config;


}
