package com.healerjean.proj.utils.ratelimit;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.healerjean.proj.config.QpsRateLimitConfiguration;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 限流工具
 *
 * @author zhangyujin
 * @date 2024/10/30
 */
@SuppressWarnings("all")
@Slf4j
@Service
@DependsOn("qpsRateLimitConfiguration")
public class QpsRateLimitUtils {

    /**
     * 限流配置
     */
    @Resource
    private QpsRateLimitConfiguration qpsRateLimitConfiguration;

    /**
     * QRS_RATE_LIMIT_MAP
     */
    private static Map<String, QpsRateLimitDTO> QRS_RATE_LIMIT_MAP = new ConcurrentHashMap();

    /**
     * isRunning
     */
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * 必须刷新才能启动
     */
    @PostConstruct
    public void init() {
        Map<String, QpsRateLimitDTO> limitConfig = qpsRateLimitConfiguration.getLimitConfig();
        try {
            if (log.isInfoEnabled()){
                log.info("limitConfig: {}", JSON.toJSONString(limitConfig));
            }
            if (CollectionUtils.isEmpty(limitConfig)){
                log.error("限流未配置, limitConfig:{}", JSON.toJSON(limitConfig));
                return;
            }
            limitConfig.forEach((k, v) -> {
                Assert.notNull(k);
                Assert.notNull(v.getLimit());
            });
            refreshRateLimitMap(qpsRateLimitConfiguration.getLimitConfig());
        } catch (Exception e) {
            log.error("限流配置加载失败, rateLimitConfig:{}", JSON.toJSON(limitConfig), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 需要支持动态更新
     */
    public void refreshRateLimitMap(Map<String, QpsRateLimitDTO> limitConfig) {
        // 赋值
        QRS_RATE_LIMIT_MAP = limitConfig.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v ->
                        new QpsRateLimitDTO()
                                .setLimit(v.getValue().getLimit())
                                .setRateLimiter(RateLimiter.create(v.getValue().getLimit()))));
    }

    /**
     * 限流配置
     *
     * @param key key
     */
    public static double acquire(String key) {
        // -1 表示无限流配置
        QpsRateLimitDTO limitConfig = QRS_RATE_LIMIT_MAP.get(key);
        if (Objects.isNull(limitConfig)) {
            return -1;
        }

        RateLimiter rateLimiter = limitConfig.getRateLimiter();
        double acquire = rateLimiter.acquire();
        if (acquire > 0 && log.isWarnEnabled()){
            log.warn("key:{},被限流:{}, rate:{} 等待:{}s", key, limitConfig.getLimit(), rateLimiter.getRate(), acquire);
        }
        if (acquire <= 0 && log.isInfoEnabled()) {
            log.info("key:{},限流值:{}, rate:{} 等待:{}s", key, limitConfig.getLimit(), rateLimiter.getRate(), acquire);
        }
        return acquire;
    }


    @Accessors(chain = true)
    @Data
    public static class QpsRateLimitDTO {


        /**
         * 限流Key
         */
        private String key;

        /**
         * 限流阈值
         */
        private Double limit;

        /**
         * 限流器
         */
        private RateLimiter rateLimiter;

    }


}
