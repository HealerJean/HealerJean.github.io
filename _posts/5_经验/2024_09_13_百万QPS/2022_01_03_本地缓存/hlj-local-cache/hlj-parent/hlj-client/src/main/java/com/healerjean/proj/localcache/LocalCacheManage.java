package com.healerjean.proj.localcache;

import java.util.Map;
import java.util.Set;

/**
 * LocalCacheManage
 *
 * @author zhangyujin
 * @date 2023/7/4$  19:43$
 */
public interface LocalCacheManage<K, V> {


    /**
     * 查询缓存
     *
     * @param keys keys
     * @return Map<K, V>
     */
    Map<K, V> getLocalCache(Set<K> keys);

    /**
     * 获取所有缓存
     *
     * @return Map<K, V>
     */
    Map<String, V> getAllLocalCache();

    /**
     * 刷新所有缓存
     */
    void refreshAll();
}
