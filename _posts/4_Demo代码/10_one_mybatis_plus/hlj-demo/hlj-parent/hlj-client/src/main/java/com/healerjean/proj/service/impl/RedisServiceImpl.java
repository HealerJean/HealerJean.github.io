package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhangyujin
 * @date 2023/6/14  09:59.
 */
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {


    @Override
    public boolean set(String key, int expire, String value) {
        log.info("[RedisService#set] key:{}, expire:{}, value:{}", key, expire, value);
        return true;
    }

    @Override
    public String get(String key) {
        String result = "success";
        log.info("[RedisService#get] key:{}, result:{}", key, result);
        return result;
    }

    @Override
    public boolean lock(String key, String threadId, long time) {
        log.info("[RedisService#lock] key:{}, threadId:{} , time:{}", key, threadId, time);
        return true;
    }

    @Override
    public boolean unLock(String key, String threadId) {
        log.info("[RedisService#unLock] key:{}, threadId:{}", key, threadId);
        return true;
    }
}
