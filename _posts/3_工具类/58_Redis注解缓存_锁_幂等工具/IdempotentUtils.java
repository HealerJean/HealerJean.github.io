package com.jd.merchant.sign.service.util;

import com.jd.merchant.sign.common.enums.RedisEnums;
import com.jd.merchant.sign.service.service.common.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * IdempotentUtils
 *
 * @author zhangyujin
 * @date 2023/12/15
 */
@Slf4j
@Service
public class IdempotentUtils {


    /**
     * redisService
     */
    @Resource
    private RedisService redisService;


    /**
     * 幂等
     *
     * @param consumer consumer
     * @param r        r
     * @param param    param
     */
    public <R> void idempotent(RedisEnums.Cache cache, Consumer<R> consumer, R r, String type, Object... param) {
        String key = redisService.get(cache.join(type, param));
        String idempotentFlag = redisService.get(key);
        if (Objects.nonNull(idempotentFlag)) {
            return;
        }
        consumer.accept(r);
        redisService.set(key, "1", cache.getExpireSec());
    }


}
