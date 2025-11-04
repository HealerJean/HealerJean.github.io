package com.healerjean.proj.hotcache.service.storage.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.healerjean.proj.hotcache.enums.StorageStrategyEnum;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OssStorageServiceStrategy implements StorageServiceStrategy {
    private static final Logger log = LoggerFactory.getLogger(OssStorageServiceStrategy.class);
    private final OSS ossClient;
    private final String bucketName;
    private final String tempDir;

    @Override
    public StorageStrategyEnum getStorageStrategyEnum() {
        return StorageStrategyEnum.OSS;
    }

    public OssStorageServiceStrategy(String endpoint, String accessKeyId, String secretAccessKey, String bucketName) {
        this.ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, secretAccessKey);
        this.bucketName = bucketName;
        this.tempDir = System.getProperty("java.io.tmpdir", "/tmp");
    }

    @Override
    public OutputStream getOutputStream(String filename) throws IOException {
        File tmp = new File(tempDir, filename + ".tmp");
        return Files.newOutputStream(tmp.toPath());
    }

    @Override
    public void publish(String tempFilename, String finalFilename) throws IOException {
        File tmpFile = new File(tempDir, tempFilename);
        if (!tmpFile.exists()) throw new IOException("Temp file not found: " + tmpFile);

        ossClient.putObject(bucketName, finalFilename, tmpFile);
        if (!tmpFile.delete()) log.warn("Failed to delete temp file: " + tmpFile);
    }

    @Override
    public List<String> listFiles(String prefix) throws IOException {
        try (Stream<OSSObjectSummary> objects = ossClient.listObjects(bucketName, prefix).getObjectSummaries().stream()) {
            return objects.map(OSSObjectSummary::getKey).collect(Collectors.toList());
        } catch (Exception e) {
            throw new IOException("List files failed", e);
        }
    }

    @Override
    public boolean exists(String filename) throws IOException {
        try {
            return ossClient.doesObjectExist(bucketName, filename);
        } catch (Exception e) {
            throw new IOException("Check exists failed", e);
        }
    }

    @Override
    public long getFileSize(String filename) throws IOException {
        try {
            OSSObject obj = ossClient.getObject(bucketName, filename);
            long size = obj.getObjectMetadata().getContentLength();
            obj.close();
            return size;
        } catch (Exception e) {
            throw new IOException("Get file size failed", e);
        }
    }

    @Override
    public String getFileMd5(String filename) throws IOException {
        try (OSSObject obj = ossClient.getObject(bucketName, filename);
             DigestInputStream dis = new DigestInputStream(obj.getObjectContent(), MessageDigest.getInstance("MD5"))) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) ;
            byte[] digest = dis.getMessageDigest().digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IOException("Compute MD5 failed", e);
        }
    }

    @Override
    public InputStream download(String filename) throws IOException {
        try {
            OSSObject obj = ossClient.getObject(bucketName, filename);
            return obj.getObjectContent();
        } catch (Exception e) {
            throw new IOException("Download failed", e);
        }
    }

    @Override
    public void delete(String filename) throws IOException {
        try {
            ossClient.deleteObject(bucketName, filename);
        } catch (Exception e) {
            throw new IOException("Delete failed", e);
        }
    }

    public void shutdown() {
        if (ossClient != null) ossClient.shutdown();
    }
}