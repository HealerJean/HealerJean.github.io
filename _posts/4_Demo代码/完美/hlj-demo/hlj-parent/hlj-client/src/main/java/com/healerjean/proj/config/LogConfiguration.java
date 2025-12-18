package com.healerjean.proj.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
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
     * getDynamicLogConfig
     *
     * @param key key
     * @return {@link DynamicLogConfigDTO}
     */
    public DynamicLogConfigDTO getDynamicLogConfig(String key) {
        return MapUtils.getObject(dynamicLogMap, key);
    }

    /**
     * enableReq
     *
     * @param key key
     * @return {@link boolean}
     */
    public boolean isEnableReq(String key){
        DynamicLogConfigDTO dynamicLogConfig = getDynamicLogConfig(key);
        if (Objects.isNull(dynamicLogConfig)){
            return true;
        }
        return dynamicLogConfig.isEnableReq();
    }

    /**
     * enableRes
     *
     * @param key key
     * @return {@link boolean}
     */
    public boolean isEnableRes(String key){
        DynamicLogConfigDTO dynamicLogConfig = getDynamicLogConfig(key);
        if (Objects.isNull(dynamicLogConfig)){
            return true;
        }
        return dynamicLogConfig.isEnableRes();
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
        private boolean enableReq;

        /**
         * 支持 res 打印
         */
        private boolean enableRes;
    }


}
