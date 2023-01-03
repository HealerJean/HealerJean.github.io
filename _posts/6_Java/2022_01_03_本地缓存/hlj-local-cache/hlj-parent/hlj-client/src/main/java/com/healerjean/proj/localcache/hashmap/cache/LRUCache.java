package com.healerjean.proj.localcache.hashmap.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LRUCache
 * 优点：简单粗暴，不需要引入第三方包，比较适合一些比较简单的场景。
 * 缺点：没有缓存淘汰策略，定制化开发成本高。
 *
 * @author zhangyujin
 * @date 2023/1/3  13:54.
 */

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * 可重入读写锁，保证并发读写安全性
     */
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    /**
     * readLock
     */
    private final Lock readLock = readWriteLock.readLock();
    /**
     * writeLock
     */
    private final Lock writeLock = readWriteLock.writeLock();

    /**
     * 缓存大小限制
     */
    private final int maxSize;

    public LRUCache(int maxSize) {
        super(maxSize + 1, 1.0f, true);
        this.maxSize = maxSize;
    }

    /**
     * @param key key
     * @return Object
     */
    @Override
    public V get(Object key) {
        readLock.lock();
        try {
            return super.get(key);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * put
     *
     * @param key   key
     * @param value value
     * @return Object
     */
    @Override
    public V put(K key, V value) {
        writeLock.lock();
        try {
            return super.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * removeEldestEntry
     *
     * @param eldest eldest
     * @return boolean
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return this.size() > maxSize;
    }
}