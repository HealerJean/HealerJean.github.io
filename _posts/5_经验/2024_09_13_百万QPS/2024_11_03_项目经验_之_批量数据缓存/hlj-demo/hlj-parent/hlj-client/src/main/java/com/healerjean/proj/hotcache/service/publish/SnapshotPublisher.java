package com.healerjean.proj.hotcache.service.publish;


import com.healerjean.proj.hotcache.config.DatasetSnapshotConfig;
import com.healerjean.proj.hotcache.config.SnapshotExecutionConfig;
import com.healerjean.proj.hotcache.constants.SnapshotPaths;
import com.healerjean.proj.hotcache.model.SnapshotFileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 将快照元数据发布到 Redis，供下游服务发现最新版本。
 */
@Component
public class SnapshotPublisher {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发布快照信息至 Redis。
     * 使用统一的元数据对象结构
     */
    public void publish( long startTime, int totalRecords, SnapshotExecutionConfig runConfig) {
        String dataSource = runConfig.getDatasetName();
        String version = runConfig.getVersion();
        DatasetSnapshotConfig config = runConfig.getDatasetSnapshotConfig();
        // 记录最新的版本
        String latestKey = String.format(SnapshotPaths.REDIS_LATEST_KEY_FORMAT, dataSource);
        redisTemplate.opsForValue().set(latestKey, version);


        // 记录最近的10个版本
        String versionsKey = String.format(SnapshotPaths.REDIS_VERSION_KEY_FORMAT, dataSource);
        // redisTemplate.opsForList().leftPush(versionsKey, version);
        // redisTemplate.opsForList().trim(versionsKey, 0, 9);

        // 将对象序列化为JSON并存储到Redis
        SnapshotFileMetadata metadata = new SnapshotFileMetadata();
        metadata.setDatasetName(dataSource);
        metadata.setVersion(version);
        metadata.setCreateTime(startTime);
        metadata.setRecordTotalCount(totalRecords);
        metadata.setConfig(config);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String metadataJson = gson.toJson(metadata);
        String infoKey = String.format(SnapshotPaths.REDIS_INFO_KEY_FORMAT, dataSource, version);
        redisTemplate.opsForValue().set(infoKey, metadataJson);

        redisTemplate.expire(latestKey, 7, TimeUnit.DAYS);
        redisTemplate.expire(infoKey, 7, TimeUnit.DAYS);

        System.out.printf("✅ 快照已发布到 Redis: %s, 版本=%s%n", dataSource, version);
    }
}
