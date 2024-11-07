package com.healerjean.proj.utils;

import com.google.common.util.concurrent.RateLimiter;
import com.healerjean.proj.config.QpsRateLimitConfiguration;
import com.healerjean.proj.data.bo.QpsRateLimitBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 限流工具
 *
 * @author zhangyujin
 * @date 2024/10/30
 */
@Slf4j
@Service
public class QpsRateLimitUtils {

    /**
     * 限流配置
     */
    @Resource
    private QpsRateLimitConfiguration qpsRateLimitConfiguration;

    /**
     * QRS_RATE_LIMIT_MAP
     */
    private static Map<String, QpsRateLimitBO> QRS_RATE_LIMIT_MAP = new ConcurrentHashMap();


    /**
     * 需要支持动态更新
     */
    @SuppressWarnings("all")
    @PostConstruct
    public void init() {
        Map<String, QpsRateLimitBO> limitConfig = qpsRateLimitConfiguration.getLimitConfig();
        QRS_RATE_LIMIT_MAP = limitConfig.entrySet().stream()
                .filter(entry -> entry.getValue().getOpenFlag())
                .collect(Collectors.toMap(Map.Entry::getKey, v ->
                        new QpsRateLimitBO()
                                .setKey(v.getKey())
                                .setOpenFlag(v.getValue().getOpenFlag())
                                .setLimitThreshold(v.getValue().getLimitThreshold())
                                .setRateLimiter(RateLimiter.create(v.getValue().getLimitThreshold()))));

    }

    /**
     * 限流配置
     *
     * @param key key
     */
    public static double acquire(String key) {
        QpsRateLimitBO qpsRateLimit = QRS_RATE_LIMIT_MAP.get(key);
        if (Objects.isNull(qpsRateLimit)){
            return -1;
        }

        RateLimiter rateLimiter = qpsRateLimit.getRateLimiter();
        double acquire = rateLimiter.acquire();
        log.info("key:{},限流值:{}, rate:{} 等待:{}s", key, qpsRateLimit.getLimitThreshold(),rateLimiter.getRate(), acquire);
        return acquire;
    }

}
