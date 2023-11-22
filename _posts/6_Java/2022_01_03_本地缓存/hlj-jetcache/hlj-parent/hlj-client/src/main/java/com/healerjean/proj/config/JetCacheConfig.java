package com.healerjean.proj.config;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.RefreshPolicy;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.embedded.LinkedHashMapCacheBuilder;
import com.alicp.jetcache.template.QuickConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * JetcacheConfig
 *
 * @author zhangyujin
 * @date 2023/11/21
 */
@Configuration
public class JetCacheConfig {

    @Bean
    public Cache<Long, Object> userCache(CacheManager cacheManager) {
        QuickConfig qc = QuickConfig.newBuilder("userCache:").expire(Duration.ofSeconds(3600))
                .cacheNullValue(Boolean.TRUE)
                // 创建一个两级缓存
                .cacheType(CacheType.REMOTE)
                // 本地缓存元素个数限制，只对CacheType.LOCAL和CacheType.BOTH有效
                //.localLimit(100)
                // 本地缓存更新后，将在所有的节点中删去缓存，以保持强一致性
                // .syncLocal(false)
                .build();
        return cacheManager.getOrCreateCache(qc);
    }


    @Bean
    public Cache<Long, Object> userCacheLinkedHashMapCache(){
        RefreshPolicy policy = RefreshPolicy.newPolicy(1, TimeUnit.MINUTES)
                .stopRefreshAfterLastAccess(30, TimeUnit.MINUTES);
        return LinkedHashMapCacheBuilder
                .createLinkedHashMapCacheBuilder()
                // .loader(key -> loadOrderSumFromDatabase(key))
                .refreshPolicy(policy)
                .buildCache();
    }



}