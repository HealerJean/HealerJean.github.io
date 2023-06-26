package com.healerjean.proj.utils.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * FileUtils
 * 文件相关工具服务
 *
 * @author zhanghanlin6
 * @date 2021/6/25
 */
@Slf4j
public class FileUtils {

    /**
     * 创建目录
     *
     * @param path   文件路径
     * @param isFile 是否是文件
     */
    public static void createDir(String path, boolean isFile) {
        createDir(new File(path), isFile);
    }

    /**
     * 创建目录
     *
     * @param file   文件对象
     * @param isFile 是否是文件
     */
    public static void createDir(File file, boolean isFile) {
        if (file.exists()) {
            return;
        }
        if (isFile) {
            createDir(file.getParentFile(), Boolean.FALSE);
            return;
        }
        boolean mkdirResult = file.mkdirs();
        log.info("createDir - mkdirResult:{},path:{}", mkdirResult, file.getAbsolutePath());
    }

    /**
     * 清理文件
     *
     * @param file 文件对象
     */
    public static void cleanFile(File file) {
        try {
            if (!file.exists()) {
                return;
            }
            if (file.isFile()) {
                boolean result = file.delete();
                log.info("文件删除结果:{},path:{}", result, file.getAbsolutePath());
                return;
            }
            File[] fileList = file.listFiles();
            if (null != fileList && fileList.length > 0) {
                for (File subFile : fileList) {
                    cleanFile(subFile);
                }
            }
            boolean result = file.delete();
            log.info("文件删除结果:{},path:{}", result, file.getAbsolutePath());
        } catch (Exception e) {
            log.info("文件删除异常:path:{}", file.getAbsolutePath(), e);
        }
    }
}