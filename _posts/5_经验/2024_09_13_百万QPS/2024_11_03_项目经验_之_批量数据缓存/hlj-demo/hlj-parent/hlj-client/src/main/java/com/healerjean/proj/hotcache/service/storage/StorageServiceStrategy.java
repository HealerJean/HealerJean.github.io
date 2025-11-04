package com.healerjean.proj.hotcache.service.storage;


import com.healerjean.proj.hotcache.enums.StorageStrategyEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 存储服务抽象接口，屏蔽底层存储细节（如本地文件、OSS、S3）。
 * 便于在不同环境（开发、测试、生产）切换实现。
 */
public interface StorageServiceStrategy {

    /**
     * 获取存储策略
     */
    StorageStrategyEnum getStorageStrategyEnum();

    /**
     * 获取指定文件名的输出流，用于写入数据。
     * @param filename 文件名。
     * @return 输出流。
     * @throws IOException 获取失败时抛出。
     */
    OutputStream getOutputStream(String filename) throws IOException;

    /**
     * 将临时文件发布为最终文件，确保原子性。
     * @param tempFilename 临时文件名。
     * @param finalFilename 最终文件名。
     * @throws IOException 发布失败时抛出。
     */
    void publish(String tempFilename, String finalFilename) throws IOException;

    /**
     * 列出指定前缀的所有文件。
     * @param prefix 文件前缀。
     * @return 文件名列表。
     * @throws IOException 列出失败时抛出。
     */
    java.util.List<String> listFiles(String prefix) throws IOException;

    /**
     * 检查文件是否存在。
     * @param filename 文件名。
     * @return true 表示存在。
     * @throws IOException 检查失败时抛出。
     */
    boolean exists(String filename) throws IOException;

    /**
     * 获取文件大小。
     * @param filename 文件名。
     * @return 文件大小（字节）。
     * @throws IOException 获取失败时抛出。
     */
    long getFileSize(String filename) throws IOException;

    /**
     * 计算并返回文件的 MD5 校验和。
     * @param filename 文件名。
     * @return MD5 字符串。
     * @throws IOException 计算失败时抛出。
     */
    String getFileMd5(String filename) throws IOException;

    /**
     * 下载文件并返回输入流。
     * @param filename 文件名。
     * @return 文件输入流。
     * @throws IOException 下载失败时抛出。
     */
    InputStream download(String filename) throws IOException;

    /**
     * 删除指定文件。
     * @param filename 文件名。
     * @throws IOException 删除失败时抛出。
     */
    void delete(String filename) throws IOException;
}
