package com.healerjean.proj.data.bo;

import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * QpsRateLimitBO
 *
 * @author zhangyujin
 * @date 2024/10/30
 */
@Accessors(chain = true)
@Data
public class QpsRateLimitBO {


    /**
     * 限流Key
     */
    private String key;

    /**
     * 是否开启限流
     */
    private Boolean openFlag;

    /**
     * 限流阈值
     */
    private Double limitThreshold;

    /**
     * 限流器
     */
    private RateLimiter rateLimiter;

}
