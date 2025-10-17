package com.healerjean.proj.cache;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * redis工具类
 */
public class RedisCache {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 向key值中设置value
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 向key值中设置value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取value的值
     *
     * @param key key
     * @return Object
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 根据key 删除对应的数据
     *
     * @param key key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }


    /**
     * 返回符合给定模式的 key 列表
     *
     * @param key key
     * @return Set<String>
     */
    public Set<String> keys(String key) {
        return redisTemplate.keys(key + "*");
    }

}
