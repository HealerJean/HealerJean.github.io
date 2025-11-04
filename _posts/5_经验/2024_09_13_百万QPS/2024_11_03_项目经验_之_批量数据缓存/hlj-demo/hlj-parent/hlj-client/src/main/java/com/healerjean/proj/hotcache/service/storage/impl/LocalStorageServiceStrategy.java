package com.healerjean.proj.hotcache.service.storage.impl;


import com.healerjean.proj.hotcache.constants.SnapshotPaths;
import com.healerjean.proj.hotcache.enums.StorageStrategyEnum;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 本地文件系统实现的存储服务。适用于开发和集成测试场景。
 *
 * @author zhangyujin
 * @date 2025-11-04 05:11:10
 */
@Slf4j
public class LocalStorageServiceStrategy implements StorageServiceStrategy {
    private static final Path SNAPSHOT_DIR = Paths.get("/Users/zhangyujin1/Desktop/logs/hotcache", "/tmp");

    static {
        try {
            Files.createDirectories(SNAPSHOT_DIR);
        } catch (java.io.IOException e) {
            throw new RuntimeException("创建本地目录失败", e);
        }
    }

    @Override
    public StorageStrategyEnum getStorageStrategyEnum() {
        return StorageStrategyEnum.LOCAL;
    }

    @Override
    public OutputStream getOutputStream(String filename) throws java.io.IOException {
        Path tempFile = SNAPSHOT_DIR.resolve(filename + SnapshotPaths.TEMP_FILE_SUFFIX);
        // 确保父目录存在
        Path parent = tempFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        return new BufferedOutputStream(new FileOutputStream(tempFile.toFile()));
    }

    @Override
    public void publish(String tempFilename, String finalFilename) throws java.io.IOException {
        Path temp = SNAPSHOT_DIR.resolve(tempFilename);
        Path target = SNAPSHOT_DIR.resolve(finalFilename);
        Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public List<String> listFiles(String prefix) throws java.io.IOException {
        try (Stream<Path> stream = Files.walk(SNAPSHOT_DIR, 5)) {
            return stream
                    .filter(path -> path.getFileName().toString().startsWith(prefix))
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean exists(String filename) throws java.io.IOException {
        return Files.exists(SNAPSHOT_DIR.resolve(filename));
    }

    @Override
    public long getFileSize(String filename) throws java.io.IOException {
        return Files.size(SNAPSHOT_DIR.resolve(filename));
    }

    @Override
    public String getFileMd5(String filename) throws java.io.IOException {
        Path path = SNAPSHOT_DIR.resolve(filename);
        try (InputStream is = Files.newInputStream(path)) {
            return org.apache.commons.codec.digest.DigestUtils.md5Hex(is);
        }
    }

    @Override
    public InputStream download(String filename) throws java.io.IOException {
        return Files.newInputStream(SNAPSHOT_DIR.resolve(filename));
    }

    @Override
    public void delete(String filename) throws java.io.IOException {
        Files.deleteIfExists(SNAPSHOT_DIR.resolve(filename));
    }
}
