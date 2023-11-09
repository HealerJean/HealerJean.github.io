package com.healerjean.proj.localcache.caffeine.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Sets;
import com.healerjean.proj.localcache.LocalCacheManage;
import com.healerjean.proj.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * AbstractLocalCacheManager
 *
 * @author zhangyujin
 * @date 2023/1/3  13:19.
 */
@Slf4j
@EnableScheduling
public abstract class AbstractLocalCacheManager<K, V> implements LocalCacheManage<K, V> {

    /**
     * IGNORE_kEY
     */
    private Set<K> ignoreKey;

    /**
     * AbstractLocalCacheManager
     */
    public AbstractLocalCacheManager() {
    }

    /**
     * AbstractLocalCacheManager
     *
     * @param ignoreKey ignoreKey
     */
    public AbstractLocalCacheManager(Set<K> ignoreKey) {
        this.ignoreKey = ignoreKey;
    }


    /**
     * 缓存容器
     */
    private final Cache<String, V> localCache = Caffeine.newBuilder()
            .maximumSize(200000)
            .recordStats()
            .build();

    /**
     * 初始化数据
     */
    @PostConstruct
    public void init() {
        refreshAll();
    }


    /**
     * 获取全部缓存信息
     *
     * @return Map<String, V>
     */
    @Override
    public Map<String, V> getAllLocalCache() {
        ConcurrentMap<String, V> concurrentMap = localCache.asMap();
        if (CollectionUtils.isEmpty(concurrentMap)) {
            refreshAll();
        }
        return localCache.asMap();
    }


    /**
     * 获取多个Keys结果
     *
     * @param keys keys
     * @return Map<K, V>
     */
    @Override
    public Map<K, V> getLocalCache(Set<K> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }
        if (!CollectionUtils.isEmpty(ignoreKey)) {
            keys.removeAll(ignoreKey);
        }
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        // 转成本地key
        Map<String, K> localKeyMap = keys.stream().collect(Collectors.toMap(this::toLocalKey, k -> k));
        // 先获取本地结果
        Map<String, V> localResultMap = localCache.getAllPresent(localKeyMap.keySet());
        // 获取除本地缓存之外的其余信息
        Map<K, V> loadResultMap = Collections.emptyMap();
        if (localKeyMap.size() > localResultMap.size()) {
            Set<String> absentLocalKeys = Sets.difference(localKeyMap.keySet(), localResultMap.keySet());
            Set<K> absentKeys = absentLocalKeys.stream().map(localKeyMap::get).collect(Collectors.toSet());
            // 存在失效的key
            if (!absentKeys.isEmpty()) {
                loadResultMap = refresh(absentKeys);
            }
        }
        // 合并结果
        Map<K, V> retMap = new HashMap<>(loadResultMap);
        localResultMap.forEach((localKey, v) -> {
            K k = localKeyMap.get(localKey);
            retMap.put(k, v);
        });
        return retMap;
    }

    /**
     * 获取单个Key
     *
     * @param k k
     * @return V
     */
    public V getLocalCache(K k) {
        if (k == null) {
            return null;
        }
        if (!CollectionUtils.isEmpty(ignoreKey) && ignoreKey.contains(k)) {
            return null;
        }
        String localKey = toLocalKey(k);
        V ret = localCache.getIfPresent(localKey);
        if (ret == null) {
            return refresh(k);
        }
        return ret;
    }

    /**
     * 重新加载Key,会对历史缓存进行清理
     *
     * @param key key
     * @return @return
     */
    public V refresh(K key) {
        try {
            V v = load(key);
            localCache.invalidate(key);
            if (v != null) {
                writeCache(key, v);
            }
            return v;
        } catch (Exception ex) {
            String errorMsg = MessageFormatter.format("加载:{}.loadKey({})到内存-异常:", new Object[]{this.getClass().getSimpleName(), key}).getMessage();
            log.error(errorMsg, ex);
            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * 重新加载多个Key,会对历史缓存进行清理
     *
     * @param keys keys
     * @return Map<K, V>
     */
    public Map<K, V> refresh(Set<K> keys) {
        try {
            Map<K, V> map = load(keys);
            localCache.invalidateAll(keys);
            if (!CollectionUtils.isEmpty(map)) {
                map.forEach(this::writeCache);
            }
            return map;
        } catch (Exception ex) {
            String errorMsg = MessageFormatter.format("加载:{}.loadKeys({})到内存-异常:", new Object[]{this.getClass().getSimpleName(), JsonUtils.toJsonString(keys)}).getMessage();
            log.error(errorMsg, ex);
            throw new RuntimeException(errorMsg);
        }

    }

    /**
     * 刷新所有Key,会对历史缓存进行清理
     */
    @Override
    public void refreshAll() {
        try {
            long t1 = System.currentTimeMillis();
            Map<K, V> map = loadAll();
            localCache.invalidateAll();
            if (!CollectionUtils.isEmpty(map)) {
                map.forEach(this::writeCache);
            }
            log.info("加载:{}.loadAll() success, stats:{},cost:{}ms", this.getClass().getSimpleName(), localCache.stats(), System.currentTimeMillis() - t1);
        } catch (Exception ex) {
            String errorMsg = MessageFormatter.format("加载:{}.loadAll()到内存-异常:", new Object[]{this.getClass().getSimpleName()}).getMessage();
            log.error(errorMsg, ex);
            throw new RuntimeException(errorMsg);
        }
    }


    /**
     * 子类实现,缓存数据加载,单Key
     *
     * @param keys
     * @return
     */
    protected abstract Map<K, V> load(Set<K> keys);

    /**
     * 子类实现,缓存数据加载,多Key
     *
     * @param key
     * @return
     */
    protected abstract V load(K key);

    /**
     * 子类实现,缓存数据加载,全部Key
     *
     * @return Map<K, V>
     */
    protected abstract Map<K, V> loadAll();

    /**
     * 缓存Key转换
     *
     * @param k k
     * @return String
     */
    private String toLocalKey(K k) {
        return k.toString();
    }

    /**
     * 写入缓存
     *
     * @param k k
     * @param v v
     */
    private void writeCache(K k, V v) {
        String localKey = toLocalKey(k);
        localCache.put(localKey, v);
    }

    /**
     * 删除缓存key
     *
     * @param k key
     */
    public void remove(K k) {
        localCache.invalidate(k);
    }

}