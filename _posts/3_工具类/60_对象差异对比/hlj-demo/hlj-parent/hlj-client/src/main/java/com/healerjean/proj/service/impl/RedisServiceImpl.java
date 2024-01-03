package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
}
