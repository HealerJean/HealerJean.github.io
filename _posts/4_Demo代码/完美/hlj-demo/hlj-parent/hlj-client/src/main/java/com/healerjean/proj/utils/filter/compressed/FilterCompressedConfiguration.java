package com.healerjean.proj.utils.filter.compressed;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * FilterCompressedConfiguration
 *
 * @author zhangyujin
 * @date 2025/1/8
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties("filter")
public class FilterCompressedConfiguration {


    /**
     * 压缩过滤器配置
     */
    private Map<String, CompressedBizBO> compressedBizMap;

}
