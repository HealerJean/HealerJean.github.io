package com.healerjean.proj.hotcache.model;


/**
 * 快照元数据对象，用于在Redis和清单文件中存储统一的元数据信息
 */

import com.healerjean.proj.hotcache.config.DatasetSnapshotConfig;
import lombok.Data;

import java.util.List;

@Data
public class SnapshotFileMetadata {
    /**
     * 数据源名称
     */
    private String datasetName;

    /**
     * 快照版本
     */
    private String version;

    /**
     * 创建时间戳
     */
    private long createTime;

    /**
     * 记录总数
     */
    private int recordTotalCount;

    /**
     * 配置信息
     */
    private DatasetSnapshotConfig config;

    /**
     * 文件信息列表
     */
    private List<FileInfo> files;

    /**
     * 清单中单个文件的元信息。
     */
    @Data
    public static class FileInfo {
        /**
         * 文件名。
         */
        public String filename;

        /**
         * 该文件包含的记录数。
         */
        public int recordCount;

        /**
         * 文件大小（字节）。
         */
        public long fileSize;

        /**
         * 文件的 MD5 校验和。
         */
        public String md5;
    }
}
