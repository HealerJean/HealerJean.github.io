package com.healerjean.proj.localcache.guavacache.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import com.healerjean.proj.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AbstractLocalCacheManager
 *
 * @author zhangyujin
 * @date 2023/1/3  13:19.
 */
@Slf4j
@EnableScheduling
public abstract class AbstractLocalCacheManager<K, V> {

    /**
     * 缓存容器
     */
    private final Cache<String, V> localCache = CacheBuilder.newBuilder()
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
     * @param k k
     * @return V
     */
    protected abstract V load(K k);

    /**
     * load
     *
     * @param keys keys
     * @return Map<K, V>
     */
    protected abstract Map<K, V> load(Set<K> keys);

    /**
     * loadAll
     *
     * @return Map<K, V>
     */
    protected abstract Map<K, V> loadAll();


    /**
     * 获取多个keys结果
     *
     * @param keys keys
     * @return Map<K, V>
     */
    public Map<K, V> getCaches(Set<K> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }
        // 转成本地key
        Map<String, K> localKeyMap = keys.stream().collect(Collectors.toMap(this::toLocalKey, k -> k));
        // 先获取本地结果
        Map<String, V> localResultMap = localCache.getAllPresent(localKeyMap.keySet());
        // reload的结果
        Map<K, V> loadResultMap = Collections.emptyMap();
        if (localKeyMap.size() > localResultMap.size()) {
            Set<String> absentLocalKeys = Sets.difference(localKeyMap.keySet(), localResultMap.keySet());
            Set<K> absentKeys = absentLocalKeys.stream().map(localKeyMap::get).collect(Collectors.toSet());
            // 存在失效的key
            if (absentKeys.size() > 0) {
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
     * 获取单个key
     *
     * @param k k
     * @return V
     */
    public V getCache(K k) {
        String localKey = toLocalKey(k);
        V ret = localCache.getIfPresent(localKey);
        if (ret == null) {
            return refresh(k);
        }
        return ret;
    }

    /**
     * 删除key
     *
     * @param keys keys
     */
    protected void delCaches(Collection<K> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        // 转成本地key
        Set<String> localKeys = keys.stream().map(this::toLocalKey).collect(Collectors.toSet());
        localCache.invalidateAll(localKeys);
    }

    /**
     * 重新加载key
     *
     * @param k k
     * @return V
     */
    public V refresh(K k) {
        V v = load(k);
        if (v != null) {
            writeCache(k, v);
        }
        return v;
    }

    /**
     * 重新加载多个key
     *
     * @param keys keys
     * @return Map<K, V>
     */
    public Map<K, V> refresh(Set<K> keys) {
        Map<K, V> map = load(keys);
        if (!CollectionUtils.isEmpty(map)) {
            map.forEach(this::writeCache);
        }
        return map;
    }

    /**
     * 刷新所有key
     */
    public void refreshAll() {
        long t1 = System.currentTimeMillis();
        Map<K, V> map = loadAll();
        if (!CollectionUtils.isEmpty(map)) {
            map.forEach(this::writeCache);
        }
        log.info("[AbstractLocalCacheManager#refreshAll] {}, loadAll use time: {}ms , total: {} , stats:{}", this.getClass().getSimpleName(), System.currentTimeMillis() - t1, localCache.size(), JsonUtils.toJsonString(localCache.stats()));
    }


    /**
     * toLocalKey
     *
     * @param k k
     * @return String
     */
    private String toLocalKey(K k) {
        return String.format("%s_%s", this.getClass().getSimpleName(), k.toString());
    }

    /**
     * 写入缓存
     *
     * @param k k
     * @param v v
     */
    protected void writeCache(K k, V v) {
        String localKey = toLocalKey(k);
        localCache.put(localKey, v);
    }
}