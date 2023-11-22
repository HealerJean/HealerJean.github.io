package com.healerjean.proj.config;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * JetcacheConfig
 *
 * @author zhangyujin
 * @date 2023/11/21
 */
@Configuration
public class JetCacheConfig {

    @Bean
    public Cache<Long, Object> getUserCache(CacheManager cacheManager) {
        QuickConfig qc = QuickConfig.newBuilder("userCache:").expire(Duration.ofSeconds(3600))
                // 创建一个两级缓存
                .cacheType(CacheType.REMOTE)
                // 本地缓存元素个数限制，只对CacheType.LOCAL和CacheType.BOTH有效
                //.localLimit(100)
                // 本地缓存更新后，将在所有的节点中删去缓存，以保持强一致性
                // .syncLocal(false)
                .build();
        return cacheManager.getOrCreateCache(qc);
    }
}