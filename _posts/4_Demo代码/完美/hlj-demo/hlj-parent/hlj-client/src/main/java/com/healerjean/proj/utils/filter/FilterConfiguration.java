package com.healerjean.proj.utils.filter;

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
public class FilterConfiguration {


    /**
     * 压缩过滤器配置
     */
    private Map<String, CompressedConfig> compressedConfigMap;


    /**
     * 配置
     */
    private Map<String, BloomFilterConfig> bloomConfigMap;


    /**
     * getCompressedConfig
     *
     * @param compressedConfigKey compressedConfigKey
     * @return {@link CompressedConfig}
     */
    public CompressedConfig getCompressedConfig(String compressedConfigKey) {
        CompressedConfig compressedConfig = compressedConfigMap.get(compressedConfigKey);
        compressedConfig.setKey(compressedConfigKey);
        return compressedConfig;
    }

    /**
     * getBloomFilterConfig
     *
     * @param bloomFilterConfigKey bloomFilterConfigKey
     * @return {@link BloomFilterConfig}
     */
    public BloomFilterConfig getBloomFilterConfig(String bloomFilterConfigKey) {
        BloomFilterConfig bloomFilterConfig = bloomConfigMap.get(bloomFilterConfigKey);
        bloomFilterConfig.setKey(bloomFilterConfigKey);
        return bloomFilterConfig;
    }

    /**
     * CompressedConfig
     */
    @Data
    public static class CompressedConfig {

        /**
         * key
         */
        private String key;

        /**
         * 过期时间
         */
        private Integer expireSeconds;

        /**
         * 桶大小
         */
        private Long bucketSize;

    }


    /**
     * BloomFilterConfig
     */
    @Data
    public static class BloomFilterConfig {

        /**
         * key
         */
        private String key;

        /**
         * 预期数量
         */
        private long expectedInsertions;

        /**
         * 错误率
         */
        private double falseProbability;

    }
}
