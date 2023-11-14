package com.healerjean.proj.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.healerjean.proj.config.properties.MultilevelCacheConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存管理器
 *
 * @author zhangyujin
 * @date 2023/11/14
 */
@Slf4j
public class MultilevelCacheManager implements CacheManager {


    /**
     * 多级缓存配置信息
     */
    private final MultilevelCacheConfig multilevelCacheConfig;

    /**
     * 一级缓存：集合
     */
    private final ConcurrentMap<String, MultilevelCache> cacheMap = new ConcurrentHashMap<String, MultilevelCache>();

    /**
     * 二级缓存：redis
     */
    private final RedisCache redisCache;

    /**
     * 是否动态根据cacheName创建Cache的实现，默认true
     */
    private final boolean dynamic;

    /**
     * 缓存名称集合
     */
    private final Set<String> cacheNames;



    public MultilevelCacheManager(MultilevelCacheConfig multilevelCacheConfig,
                                  RedisCache redisCache) {
        super();
        this.multilevelCacheConfig = multilevelCacheConfig;
        this.dynamic = multilevelCacheConfig.isDynamic();
        this.cacheNames = multilevelCacheConfig.getCacheNames();
        this.redisCache = redisCache;
    }


    /**
     * 获取缓存实现类
     *
     * @param name name
     * @return {@link Cache}
     */
    @Override
    public MultilevelCache getCache(String name) {
        MultilevelCache cache = cacheMap.get(name);
        if (cache != null) {
            return cache;
        }
        if (!dynamic && !cacheNames.contains(name)) {
            return cache;
        }

        cache = new MultilevelCache(name, redisCache, caffeineCache(), multilevelCacheConfig);
        MultilevelCache oldCache = cacheMap.putIfAbsent(name, cache);
        log.debug("create cache instance, the cache name is : {}", name);
        return oldCache == null ? cache : oldCache;
    }


    /**
     * 获取管理的缓存名称集合
     */
    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }


    /**
     * 构建caffeineCache
     */
    public com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache() {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if (multilevelCacheConfig.getCaffeineConfig().getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(multilevelCacheConfig.getCaffeineConfig().getExpireAfterAccess(), TimeUnit.SECONDS);
        }
        if (multilevelCacheConfig.getCaffeineConfig().getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(multilevelCacheConfig.getCaffeineConfig().getExpireAfterWrite(), TimeUnit.SECONDS);
        }
        if (multilevelCacheConfig.getCaffeineConfig().getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(multilevelCacheConfig.getCaffeineConfig().getInitialCapacity());
        }
        if (multilevelCacheConfig.getCaffeineConfig().getMaximumSize() > 0) {
            cacheBuilder.maximumSize(multilevelCacheConfig.getCaffeineConfig().getMaximumSize());
        }
        if (multilevelCacheConfig.getCaffeineConfig().getRefreshAfterWrite() > 0) {
            cacheBuilder.refreshAfterWrite(multilevelCacheConfig.getCaffeineConfig().getRefreshAfterWrite(), TimeUnit.SECONDS);
        }
        return cacheBuilder.build();
    }


}
