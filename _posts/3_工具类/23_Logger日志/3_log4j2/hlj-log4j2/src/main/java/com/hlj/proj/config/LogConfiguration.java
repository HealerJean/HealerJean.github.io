package com.hlj.proj.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Objects;

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
     * enableReq
     *
     * @param key key
     * @return {@link boolean}
     */
    public Pair<Boolean, Boolean> dynamicLogRes(String key) {
        DynamicLogConfigDTO keyLogConfig = MapUtils.getObject(dynamicLogMap, key);
        if (Objects.isNull(keyLogConfig)) {
            return null;
        }
        return Pair.of(keyLogConfig.getEnableReq(), keyLogConfig.getEnableRes());
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
    }


}
