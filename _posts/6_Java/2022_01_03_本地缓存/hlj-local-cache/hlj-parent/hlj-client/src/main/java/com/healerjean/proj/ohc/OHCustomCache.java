package com.healerjean.proj.ohc;

import org.caffinitas.ohc.Eviction;
import org.caffinitas.ohc.OHCache;
import org.caffinitas.ohc.OHCacheBuilder;
import org.junit.Test;

/**
 * OHCustomCache
 *
 * @author zhangyujin
 * @date 2025/7/7
 */
public class OHCustomCache {


    @Test
    public void test(){
        // 创建OHCache实例
        OHCache<String, String> ohCache = OHCacheBuilder.<String, String>newBuilder()
                .keySerializer(new StringSerializer()) // 设置键序列化器
                .valueSerializer(new StringSerializer()) // 设置值序列化器
                .capacity(1024 * 1024 * 1024) // 设置缓存容量为1GB
                .eviction(Eviction.LRU) // 设置缓存逐出策略为LRU
                .segmentCount(512) // 设置分段数，提高并发性能
                .build();

        // 使用OHCache
        ohCache.put("key1", "value1");
        String value = ohCache.get("key1");
        System.out.println("Value for key1: " + value);
    }


}
