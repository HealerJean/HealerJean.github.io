package com.healerjean.proj.cache;


import com.github.benmanes.caffeine.cache.Cache;
import com.healerjean.proj.config.properties.MultilevelCacheConfig;
import com.healerjean.proj.message.CacheMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2022/7/20 17:03
 */
@Slf4j
public class MultilevelCache extends AbstractValueAdaptingCache {


    /**
     * 缓存名称
     */
    private final String cacheName;

    /**
     * 一级缓存
     */
    private final Cache<Object, Object> caffeineCache;

    /**
     * 二级缓存实例
     */
    private final RedisCache redisCache;

    /**
     * 组合缓存配置
     */
    private final MultilevelCacheConfig.CompositeConfig compositeConfig;

    /**
     * 一级缓存-配置
     */
    private final MultilevelCacheConfig.RedisConfig redisConfig;


    public MultilevelCache(String cacheName,
                           RedisCache redisCache,
                           Cache<Object, Object> caffeineCache,
                           MultilevelCacheConfig multilevelCacheConfig) {
        super(multilevelCacheConfig.isAllowNullValues());
        this.cacheName = cacheName;
        this.redisCache = redisCache;
        this.caffeineCache = caffeineCache;
        this.compositeConfig = multilevelCacheConfig.getCompositeConfig();
        this.redisConfig = multilevelCacheConfig.getRedisConfig();
    }

    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }


    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        return (T) value;
    }


    /**
     * 1、获取缓存key
     * 2、从一级缓存获取
     * 2.1、判断是否开启一级缓存
     * 2.2、一级缓存开启，从一级缓存获取，一级缓存获取不到从二级缓存获取
     * 3、从二级缓存获取
     * 3.1、二级缓存获取到则判断一级缓存是否开启，如果开启，则更新一级缓存
     * 3.2、返回二级缓存数据
     */
    @Override
    protected Object lookup(Object key) {
        // 1、获取缓存key
        String cacheKey = getKey(key);

        // 2.1、判断是否开启一级缓存
        boolean ifL1Open = level1Switch(cacheKey);

        // 2.2、一级缓存开启，从一级缓存获取，一级缓存获取不到从二级缓存获取
        if (ifL1Open) {
            // 从L1获取缓存
            Object value = caffeineCache.getIfPresent(key);
            if (value != null) {
                if (log.isDebugEnabled()) {
                    log.debug("caffeineCache get cache, cacheName={}, key={}, value={}", this.getName(), key, value);
                }
                return value;
            }
        }

        // 3、从二级缓存获取
        Object value = redisCache.get(cacheKey);
        // 3.1、二级缓存获取到则判断一级缓存是否开启，如果开启，则更新一级缓存
        if (value != null && ifL1Open) {
            if (log.isDebugEnabled()) {
                log.debug("level2Cache get cache and put in level1Cache, cacheName={}, key={}, value={}", this.getName(), key, value);
            }
            caffeineCache.put(key, toStoreValue(value));
        }
        // 3.2、返回二级缓存数据
        return value;
    }


    /**
     * 1、如果value为空，但是value不能放空，那么把数据情掉就好。
     * 2、根据redis过期时间缓存数据
     * 3、如果开启一级缓存，则缓存，并发送通知消息，更新其他节点一级缓存
     */
    @Override
    public void put(Object key, Object value) {
        // 1、如果value为空，但是value不能放空，那么把数据情掉就好。
        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }

        // 2、根据redis过期时间缓存数据
        long expire = getExpire();
        if (expire > 0) {
            redisCache.set(getKey(key), toStoreValue(value), expire);
        } else {
            redisCache.set(getKey(key), toStoreValue(value));
        }

        // 3、如果开启一级缓存，则缓存，并发送通知消息，更新其他节点一级缓存
        boolean ifL1Open = level1Switch(getKey(key));
        if (ifL1Open) {
            asyncPublish(new CacheMessage().setCacheName(cacheName).setKey(key));
            caffeineCache.put(key, toStoreValue(value));
        }
    }

    /**
     * key不存在时，再保存，存在返回当前值不覆盖
     * 1、从redis获取缓存值，如果存在则直接返回
     * 2、redis中不存在，则放入redis缓存
     * 3、发送缓存变更消息，清理一级本地缓存
     */
    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        // 1、从redis获取缓存值，如果存在则直接返回
        String cacheKey = getKey(key);
        Object prevValue = redisCache.get(cacheKey);
        if (Objects.nonNull(prevValue)) {
            return toValueWrapper(prevValue);
        }

        // 2、redis中不存在，则放入redis缓存
        long expire = getExpire();
        if (expire > 0) {
            redisCache.set(getKey(key), toStoreValue(value), expire);
        } else {
            redisCache.set(getKey(key), toStoreValue(value));
        }

        // 3、发送缓存变更消息，清理一级本地缓存
        asyncPublish(new CacheMessage().setCacheName(cacheName).setKey(key));
        caffeineCache.put(key, toStoreValue(value));
        return toValueWrapper(prevValue);
    }

    /**
     * 1、删除redis数据，然后通过消息推送清除所有节点caffeine中的缓存（避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中）
     * 2、发送缓存变更消息，清理一级本地缓存
     */
    @Override
    public void evict(Object key) {
        // 1、删除redis数据，然后通过消息推送清除所有节点caffeine中的缓存（避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中）
        redisCache.delete(getKey(key));

        // 2、发送缓存变更消息，清理一级本地缓存
        boolean ifL1Open = level1Switch(getKey(key));
        if (ifL1Open) {
            asyncPublish(new CacheMessage().setCacheName(cacheName).setKey(key));
            caffeineCache.invalidate(key);
        }
    }


    /**
     * 1、清理redis缓存数据（先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中）
     * 2、发送缓存变更消息，清理一级本地缓存
     */
    @Override
    public void clear() {
        // 1、清理redis缓存数据（先清除redis中缓存数据，然后清除caffeine中的缓存，避免短时间内如果先清除caffeine缓存后其他请求会再从redis里加载到caffeine中
        Set<String> keys = redisCache.keys(this.cacheName.concat(":*"));
        for (String key : keys) {
            redisCache.delete(key);
        }

        // 2、发送缓存变更消息，清理一级本地缓存
        asyncPublish(new CacheMessage().setCacheName(cacheName).setKey(null));
    }


    /**
     * clearLocal
     *
     * @param key key
     */
    public void clearLocal(Object key) {
        log.debug("clear local cache, the key is : {}", key);
        if (key == null) {
            caffeineCache.invalidateAll();
        } else {
            caffeineCache.invalidate(key);
        }
    }


    /**
     * 获取缓存key
     *
     * @param key key
     * @return String
     */
    private String getKey(Object key) {
        return this.cacheName.concat(":").concat(key.toString());
    }

    private long getExpire() {
        long expire = redisConfig.getDefaultExpiration();
        Long cacheNameExpire = redisConfig.getExpires().get(this.cacheName);
        return cacheNameExpire == null ? expire : cacheNameExpire.longValue();
    }


    /**
     * 查询是否开启一级缓存
     * 1、检查缓存name一级缓存，开启则直接返回true
     * 2、检查缓存key的一级缓存
     *
     * @param key 缓存key
     * @return boolean
     */
    private boolean level1Switch(Object key) {
        // 1、一级缓存通过缓存名称判断开启则直接返回true
        boolean level1Switch = level1Switch();
        if (Boolean.TRUE.equals(level1Switch)) {
            return true;
        }

        // 2、检查缓存key是否开启
        return cacheKeyLevel1Switch(key);
    }


    /**
     * 本地缓存检测，检测开关与缓存名称
     * 1、如果全部启用一级缓存，直接返回true
     * 2.1、手动匹配缓存名字集合，针对cacheName维度
     *
     * @return 一级缓存是否开启
     */
    private boolean level1Switch() {
        // 1、如果全部启用一级缓存，直接返回true
        if (compositeConfig.isLevel1AllSwitch()) {
            return true;
        }
        // 2、如果配置手动启动一级缓存false，则直接返回false
        if (Boolean.FALSE.equals(compositeConfig.isLevel1Switch())) {
            return false;
        }
        // 2.1、手动匹配缓存名字集合，针对cacheName维度
        Set<String> level1SwitchCacheNames = compositeConfig.getLevel1SwitchCacheNames();
        if (CollectionUtils.isEmpty(level1SwitchCacheNames)) {
            return false;
        }
        return level1SwitchCacheNames.contains(this.getName());
    }


    /**
     * 本地缓存检测，检测key
     *
     * @param key key
     * @return boolean
     */
    private boolean cacheKeyLevel1Switch(Object key) {
        boolean level1Switch = compositeConfig.isLevel1Switch();
        if (Boolean.FALSE.equals(level1Switch)) {
            return false;
        }
        // 手动匹配缓存key集合，针对单个key维度
        Set<String> level1SwitchKeys = compositeConfig.getLevel1SwitchKeys();
        if (CollectionUtils.isEmpty(level1SwitchKeys)) {
            return false;
        }
        return level1SwitchKeys.contains(getKey(key));
    }


    /**
     * 缓存变更时通知其他节点清理本地缓存
     * 异步通过发布订阅主题消息，其他节点监听到之后进行相关本地缓存操作，防止本地缓存脏数据
     */
    void asyncPublish(CacheMessage cacheMessage) {

    }

}
