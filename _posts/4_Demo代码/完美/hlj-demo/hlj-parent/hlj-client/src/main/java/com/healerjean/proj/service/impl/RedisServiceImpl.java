package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhangyujin
 * @date 2023/6/14  09:59.
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {


    /**
     * redisTemplate
     */
    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @LogIndex
    @Override
    public boolean set(String key, String value, long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("[RedisService#set] error, key:{}, value:{}", key, value, e);
            return false;
        }
        return true;
    }


    @LogIndex
    @Override
    public String get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("[RedisService#get] error, key:{}", key, e);
            return null;
        }
    }

    @Override
    public Long zAdd(String key, Map<String, Double> members) {
        Set<ZSetOperations.TypedTuple<String>> values = members.keySet().stream().map(memberKey-> new DefaultTypedTuple<>(memberKey,members.get(memberKey))).collect(Collectors.toSet());
        return redisTemplate.opsForZSet().add(key, values);
    }

    @Override
    public Set<String> zRange(String key, long start, long end) {
        return null;
    }

    @Override
    public Long zCard(String key) {
        return null;
    }

    @Override
    public boolean expire(String key, int seconds) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, seconds, TimeUnit.SECONDS));
    }

    @LogIndex
    @Override
    public boolean lock(String key, String threadId, long time) {
        return Boolean.TRUE;
    }


    @LogIndex
    @Override
    public boolean unLock(String key, String threadId) {
        return true;
    }

    @LogIndex
    @Override
    public void setBit(String bitKey, long bucketOffset, boolean value) {
        redisTemplate.opsForValue().setBit(bitKey, bucketOffset, value);
    }
    @LogIndex
    @Override
    public Boolean getBit(String bitKey, long bucketOffset) {
        return redisTemplate.opsForValue().getBit(bitKey, bucketOffset);
    }


}
