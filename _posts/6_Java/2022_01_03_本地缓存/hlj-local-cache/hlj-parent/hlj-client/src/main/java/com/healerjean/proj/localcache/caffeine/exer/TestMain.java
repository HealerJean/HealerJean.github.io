package com.healerjean.proj.localcache.caffeine.exer;

import com.github.benmanes.caffeine.cache.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;
import org.checkerframework.checker.units.qual.K;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * TestMain
 *
 * @author zhangyujin
 * @date 2023/1/3  15:48.
 */
public class TestMain {


    /**
     * 一、Cache
     * 说明Cache 接口提供了显式搜索查找、更新和移除缓存元素的能力。当缓存的元素无法生成或者在生成的过程中抛出异常而导致生成元素失败，cache.get 也许会返回 null 。
     * 1、查找一个缓存元素， 没有查找到的时候返回null
     * 2、查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
     * 3、添加或者更新一个缓存元素
     * 4、移除一个缓存元素
     */
    @Test
    public void test() {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
        String key = "KeyName";

        // 1、查找一个缓存元素， 没有查找到的时候返回null
        String value = cache.getIfPresent(key);

        // 2、查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
        value = cache.get(key, k -> createExpensiveValue(key));

        // 3、添加或者更新一个缓存元素
        cache.put(key, value);

        // 4、移除一个缓存元素
        cache.invalidate(key);
    }

    /**
     * createExpensiveValue
     *
     * @param key key
     * @return String
     */
    private String createExpensiveValue(String key) {
        return RandomUtils.nextInt() + key;
    }


    /**
     * 二、LoadingCache
     * 说明：一个LoadingCache是一个Cache 附加上 CacheLoader能力之后的缓存实现。如果缓存不错在，则会通过CacheLoader.load来生成对应的缓存元素。
     */
    @Test
    public void loadingCache() {
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(this::createExpensiveValue);
        // 批量查找缓存，如果缓存不存在则生成缓存元素
        List<String> keys = Lists.newArrayList("key1", "key2");
        Map<String, String> valueMap = cache.getAll(keys);

        cache.asMap().get("key");
        String key = cache.get("key");
    }


    /**
     * 三、asyncCache
     * 说明：
     * 1、AsyncCache就是Cache的异步形式，提供了Executor生成缓存元素并返回CompletableFuture的能力。
     * 2、默认的线程池实现是 ForkJoinPool.commonPool() ，当然你也可以通过覆盖并实现 Caffeine.executor(Executor)方法来自定义你的线程池选择。
     * 功能：
     * 1、查找一个缓存元素， 没有查找到的时候返回null
     * 2、查找缓存元素，如果不存在，则异步生成
     * 3、添加或者更新一个缓存元素
     * 4、移除一个缓存元素
     */
    @Test
    public void asyncCache() {
        AsyncCache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .buildAsync();
        String key = "KeyName";

        // 1、查找一个缓存元素， 没有查找到的时候返回null
        CompletableFuture<String> graph = cache.getIfPresent(key);
        // 2、查找缓存元素，如果不存在，则异步生成
        graph = cache.get(key, k -> createExpensiveValue(key));
        // 3、添加或者更新一个缓存元素
        cache.put(key, graph);
        // 4、移除一个缓存元素
        cache.synchronous().invalidate(key);
    }


    /**
     * asyncLoadingCache
     * 说明：AsyncLoadingCache就是LoadingCache的异步形式，提供了异步load生成缓存元素的功能。
     * 功能：
     * 1、查找缓存元素，如果其不存在，将会异步进行生成
     * 2、批量查找缓存元素，如果其不存在，将会异步进行生成
     */
    @Test
    public void asyncLoadingCache() {

        AsyncLoadingCache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 你可以选择: 去异步的封装一段同步操作来生成缓存元素
                .buildAsync(this::createExpensiveValue);
                // 你也可以选择: 构建一个异步缓存元素操作并返回一个future
                // .buildAsync((key, executor) -> createExpensiveGraphAsync(key, executor));


        // 1、查找缓存元素，如果其不存在，将会异步进行生成
        String key = "KeyName";
        CompletableFuture<String> graph = cache.get(key);

        // 2、批量查找缓存元素，如果其不存在，将会异步进行生成
        List<String> keys = Lists.newArrayList("key1", "key2");
        CompletableFuture<Map<String, String>> graphs = cache.getAll(keys);
    }



}
