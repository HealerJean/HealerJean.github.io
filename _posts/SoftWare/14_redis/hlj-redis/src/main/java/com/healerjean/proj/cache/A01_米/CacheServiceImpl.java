package com.healerjean.proj.cache.A01_米;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author HealerJean
 * @date 2020/12/14  10:39.
 * @description
 */
@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    /**
     * Redis系统标识
     */
    public static final String REDIS_HLJ = "HLJ";
    public static final String REDIS_LOCK = "LOCK";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }


    @Override
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    @Override
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public boolean lock(String key, long timeout, TimeUnit timeUnit) {
        try {
            Long lock = increment(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, 1);
            if (lock == 1) {
                expire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeout, timeUnit);
                return true;
            } else {
                //这里相当将永不过期的key设置为过期
                Long expire = redisTemplate.getExpire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeUnit);
                if (expire != null && expire.equals(-1L)) {
                    expire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeout, timeUnit);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            delete(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key);
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        delete(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key);
    }
}
