package com.healerjean.proj.utils.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * QPS限流配置数据传输对象
 * <p>
 * 用于封装QPS限流的相关配置信息，包括限流阈值和对应的限流器实例。
 * </p>
 *
 * @author zhangyujin
 * @date 2025/4/22
 */
@SuppressWarnings("all")
@Accessors(chain = true)
@Data
public class QpsRateLimitDTO implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * QPS限流阈值，表示每秒允许通过的请求数量上限
     */
    private Double limit;

    /**
     * Guava RateLimiter限流器实例，基于令牌桶算法实现
     */
    private RateLimiter rateLimiter;

    /**
     * 默认构造函数
     */
    public QpsRateLimitDTO() {
    }

    /**
     * 带参数的构造函数
     *
     * @param limit       QPS限流阈值
     * @param rateLimiter 限流器实例
     */
    public QpsRateLimitDTO(Double limit, RateLimiter rateLimiter) {
        this.limit = limit;
        this.rateLimiter = rateLimiter;
    }


    /**
     * 创建QpsRateLimitDTO实例的静态工厂方法
     *
     * @param limit QPS限流阈值
     * @return QpsRateLimitDTO实例
     */
    public static QpsRateLimitDTO create(Double limit) {
        if (limit == null || limit <= 0) {
            throw new IllegalArgumentException("QPS限流阈值必须大于0");
        }
        return new QpsRateLimitDTO(limit, RateLimiter.create(limit));
    }
}