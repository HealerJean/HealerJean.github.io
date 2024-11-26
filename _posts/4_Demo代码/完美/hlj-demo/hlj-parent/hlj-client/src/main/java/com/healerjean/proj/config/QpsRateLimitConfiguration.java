package com.healerjean.proj.config;

import com.healerjean.proj.utils.QpsRateLimitUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * QpsRateLimitConfiguration
 *
 * @author zhangyujin
 * @date 2024/10/30
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties("qps")
public class QpsRateLimitConfiguration {

    /**
     * 限流集合
     */
    private Map<String, QpsRateLimitUtils.QpsRateLimitDTO> limitConfig;

}
