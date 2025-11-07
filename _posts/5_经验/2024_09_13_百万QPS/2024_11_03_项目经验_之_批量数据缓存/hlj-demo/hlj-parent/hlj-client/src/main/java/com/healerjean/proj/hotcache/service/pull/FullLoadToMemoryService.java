package com.healerjean.proj.hotcache.service.pull;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.healerjean.proj.hotcache.config.DatasetSnapshotConfig;
import com.healerjean.proj.hotcache.config.SnapshotGlobalConfig;
import com.healerjean.proj.hotcache.enums.SnapshotPathEnum;
import com.healerjean.proj.hotcache.factory.SnapshotFactory;
import com.healerjean.proj.hotcache.model.SnapshotMetadata;
import com.healerjean.proj.hotcache.model.UserTag;
import com.healerjean.proj.hotcache.service.cache.InMemoryUserTagCache;
import com.healerjean.proj.hotcache.service.serialization.DataSerializerStrategy;
import com.healerjean.proj.hotcache.service.storage.StorageServiceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * FullLoadToMemoryService
 *
 * @author zhangyujin
 * @date 2025/11/6
 */
@Service
@Slf4j
public class FullLoadToMemoryService {

    @Autowired
    private SnapshotGlobalConfig snapshotGlobalConfig;

    @Autowired
    private InMemoryUserTagCache userTagCache;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public void loadLatestFullSnapshotToMemory(String datasetName) throws Exception {
        // 1. 从 Redis 获取最新版本
        String latestKey = SnapshotPathEnum.REDIS_SNAPSHOT_LATEST_VERSION_KEY.format(datasetName);
        String latestVersion = redisTemplate.opsForValue().get(latestKey);
        if (latestVersion == null) {
            throw new IllegalStateException("No full snapshot found in Redis");
        }

        // 2. 获取元数据（含 manifestUrl）
        String metaKey = SnapshotPathEnum.REDIS_SNAPSHOT_VERSION_META_KEY.format(datasetName, latestVersion);
        String metaJson = redisTemplate.opsForValue().get(metaKey);
        SnapshotMetadata metadata = new Gson().fromJson(metaJson, SnapshotMetadata.class);

        // 3. 根据 storageStrategy 下载所有分片文件并反序列化
        DatasetSnapshotConfig snapshotConfig = metadata.getConfig();
        StorageServiceStrategy storage = SnapshotFactory.getInstance().getStorageServiceStrategy(datasetName, snapshotConfig.getStorageStrategy());
        DataSerializerStrategy dataSerializer = SnapshotFactory.getInstance().getDataSerializer(datasetName, snapshotConfig.getSerializerStrategy());
        for (SnapshotMetadata.FileInfo fileInfo : metadata.getFileInfoMap().values()) {
            try (InputStream is = storage.download(fileInfo.getFilename())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        try {
                            UserTag userTag = (UserTag)dataSerializer.deserialize(line.getBytes(StandardCharsets.UTF_8));
                            userTagCache.put(userTag.getUserId(), userTag);
                        }catch (Exception e){
                            log.error("line:{}, fileInfo:{}", line, JSON.toJSONString(fileInfo), e);
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        log.info("✅ 全量加载完成，共 {} 条记录", userTagCache.getAll().size());
    }
}