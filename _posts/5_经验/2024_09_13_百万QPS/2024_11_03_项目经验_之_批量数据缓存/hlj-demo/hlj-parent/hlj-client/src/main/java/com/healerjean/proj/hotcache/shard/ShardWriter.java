package com.healerjean.proj.hotcache.shard;


import com.healerjean.proj.hotcache.constants.SnapshotPaths;
import com.healerjean.proj.hotcache.service.serialization.DataSerializerStrategy;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 单个分片文件的写入器，负责将数据写入特定 shard。
 * 支持 GZIP 压缩，并确保临时文件原子发布。
 *
 * @param <T> 数据类型。
 */
@Slf4j
public class ShardWriter<T> implements java.io.Closeable {
    private final int shardId;
    private final String filename;
    private final StorageServiceStrategy storage;
    private final DataSerializerStrategy<T> serializer;
    private OutputStream outputStream;
    private boolean closed = false;
    private final java.util.concurrent.atomic.AtomicInteger recordCount = new java.util.concurrent.atomic.AtomicInteger(0);

    /**
     * 构造函数。
     *
     * @param shardId 分片编号（0 ~ N-1）。
     * @param filename 输出文件名。
     * @param storage 存储服务。
     * @param serializer 序列化器。
     * @param compress 是否压缩
     * @throws IOException 初始化失败时抛出。
     */
    public ShardWriter(int shardId, String filename, StorageServiceStrategy storage, DataSerializerStrategy<T> serializer, boolean compress) throws IOException {
        this.shardId = shardId;
        this.filename = filename;
        this.storage = storage;
        this.serializer = serializer;

        // 根据配置决定是否使用压缩
        java.io.OutputStream rawStream = storage.getOutputStream(filename);
        if (compress) {
            this.outputStream = new java.util.zip.GZIPOutputStream(rawStream);
        } else {
            this.outputStream = rawStream;
        }
    }

    /**
     * 添加一条记录到当前分片。
     *
     * @param record 要写入的记录。
     * @throws IOException 写入失败时抛出。
     */
    public void add(T record) throws IOException {
        if (closed) throw new IOException("ShardWriter 已关闭，无法继续写入");
        serializer.write(record, outputStream);
        recordCount.incrementAndGet();
    }

    @Override
    public void close() throws IOException {
        if (closed) return;
        try {
            outputStream.flush();
        } finally {
            try {
                outputStream.close();
            } finally {
                closed = true;
            }
        }
        storage.publish(filename + SnapshotPaths.TEMP_FILE_SUFFIX, filename);
    }

    /**
     * 获取当前分片已写入的记录总数。
     *
     * @return 条数。
     */
    public int getRecordCount() { return recordCount.get(); }

    public String getFilename() { return filename; }
}
