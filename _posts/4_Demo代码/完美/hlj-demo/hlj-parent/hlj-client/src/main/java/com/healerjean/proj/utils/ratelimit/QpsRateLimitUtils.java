package com.healerjean.proj.utils.ratelimit;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.config.QpsRateLimitConfiguration;
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
        // 使用QpsRateLimitDTO的静态工厂方法创建实例
        QRS_RATE_LIMIT_MAP = limitConfig.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v ->
                        QpsRateLimitDTO.create(v.getValue().getLimit())));
    }


    /**
     * 限流配置
     *
     * @param key key
     * @return 等待时间，-1表示无限流配置
     */
    public static double acquire(String key) {
        // -1 表示无限流配置
        QpsRateLimitDTO qpsRateLimit = QRS_RATE_LIMIT_MAP.get(key);
        if (Objects.isNull(qpsRateLimit)) {
            return -1;
        }

        double acquire = qpsRateLimit.getRateLimiter().acquire();
        if (acquire > 0 && log.isInfoEnabled()) {
            log.info("key:{},被限流:{}, rate:{} 等待:{}s", key, qpsRateLimit.getLimit(),
                    qpsRateLimit.getRateLimiter().getRate(), acquire);
        }
        if (acquire <= 0 && log.isDebugEnabled()) {
            log.debug("key:{},限流值:{}, rate:{} 等待:{}s", key, qpsRateLimit.getLimit(),
                    qpsRateLimit.getRateLimiter().getRate(), acquire);
        }
        return acquire;
    }




}
