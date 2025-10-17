package com.healerjean.proj.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 一二级缓存属性配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cache")
public class MultilevelCacheProperties {

    /**
     * 缓存配置
     */
    private MultilevelCacheConfig config;

}
