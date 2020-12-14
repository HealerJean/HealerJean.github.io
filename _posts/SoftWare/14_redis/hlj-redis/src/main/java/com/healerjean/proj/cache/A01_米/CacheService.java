package com.healerjean.proj.cache.A01_米;

import java.util.concurrent.TimeUnit;

/**
 * @author HealerJean
 * @date 2020/12/14  10:39.
 * @description
 */
public interface CacheService {


    /**
     * 存放缓存数据
     */
    void set(String key, Object value, long timeout, TimeUnit timeUnit);

    /**
     * 存放不过期数据
     **/
    void set(String key, Object value);


    /**
     * 获取缓存数据
     */
    Object get(String key);

    /**
     * 删除缓存数据
     */
    void delete(String key);

    /**
     * 缓存计数
     */
    Long increment(String key, long number);

    /**
     * 设置过期时间
     */
    void expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * 分布式锁
     *
     * @param key
     * @return
     */
    boolean lock(String key, long timeout, TimeUnit timeUnit);

    /**
     * 分布式锁-释放锁
     *
     * @param key
     * @return
     */
    void unlock(String key);
}
