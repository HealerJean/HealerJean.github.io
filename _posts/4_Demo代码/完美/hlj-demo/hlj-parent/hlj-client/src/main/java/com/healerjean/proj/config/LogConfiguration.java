package com.healerjean.proj.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LogConfiguration
 *
 * @author zhangyujin
 * @date 2025/12/18
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties("log")
public class LogConfiguration {

    /**
     * 动态日志配置
     */
    private Map<String, DynamicLogConfigDTO> dynamicLogMap;

    /**
     * 动态日志配置结果（请求/响应开关）
     *
     * @param key 方法唯一标识（类名#方法名）
     * @return {@link Pair<Boolean, Boolean>} 左：是否打印请求，右：是否打印响应
     */
    public Pair<Boolean, Boolean> dynamicLogRes(String key) {
        DynamicLogConfigDTO keyLogConfig = MapUtils.getObject(dynamicLogMap, key);
        if (Objects.isNull(keyLogConfig)) {
            return null;
        }
        return Pair.of(keyLogConfig.getEnableReq(), keyLogConfig.getEnableRes());
    }

    /**
     * 判断当前请求是否命中采样率（需要打印日志）
     * <p>
     * 采样逻辑：
     * <ul>
     *     <li>未配置采样率（null）或采样率为 1.0：全量打印（命中）</li>
     *     <li>采样率在 (0, 1) 之间：按概率判定</li>
     *     <li>采样率为 0：不打印（不命中）</li>
     * </ul>
     *
     * @param key 方法唯一标识（类名#方法名）
     * @return true-命中采样，需要打印日志；false-未命中，跳过日志打印
     */
    public boolean shouldSample(String key) {
        DynamicLogConfigDTO config = MapUtils.getObject(dynamicLogMap, key);
        if (Objects.isNull(config)) {
            // 未配置时默认全量打印
            return true;
        }

        Double sampleRate = config.getSampleRate();
        if (Objects.isNull(sampleRate)) {
            // 采样率为 null 时默认全量打印
            return true;
        }

        // 采样率 <= 0，不打印
        if (sampleRate <= 0) {
            return false;
        }

        // 采样率 >= 1，全量打印
        if (sampleRate >= 1.0) {
            return true;
        }

        // 按概率采样：使用 ThreadLocalRandom 提升性能
        return ThreadLocalRandom.current().nextDouble() < sampleRate;
    }


    /**
     * LogConfiguration
     *
     * @author zhangyujin
     * @date 2025-12-18 11:12:00
     */
    @Data
    public static class DynamicLogConfigDTO {

        /**
         * 支持req打印
         */
        private Boolean enableReq;

        /**
         * 支持 res 打印
         */
        private Boolean enableRes;

        /**
         * 采样率（0.0-1.0），默认 1.0 表示 100% 打印
         * 例如：0.1 表示 10% 的请求会打印日志
         */
        private Double sampleRate;
    }


}
