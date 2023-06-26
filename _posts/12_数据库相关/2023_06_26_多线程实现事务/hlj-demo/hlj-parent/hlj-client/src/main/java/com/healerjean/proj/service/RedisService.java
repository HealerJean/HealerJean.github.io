package com.healerjean.proj.service;

/**
 * RedisService 缺失实现类
 *
 * @author zhangyujin
 * @date 2023/5/26  11:29
 */
public interface RedisService {

    /**
     * 写入缓存数据
     *
     * @param key      缓存Key
     * @param value    缓存Value
     * @param timeout  缓存时间
     */
    boolean set(String key, String value, long timeout);

    /**
     * 获取缓存数据
     *
     * @param key 缓存Key
     * @return 缓存Value
     */
    String get(String key);

    /**
     * 加锁
     *
     * @param key      加锁的Key
     * @param threadId 标识设备
     * @param time     加锁时间
     * @return 加锁结果
     */
    boolean lock(String key, String threadId, long time);

    /**
     * 解锁
     *
     * @param key      加锁的Key
     * @param threadId 标识设备
     * @return 解锁结果
     */
    boolean unLock(String key, String threadId);
}
