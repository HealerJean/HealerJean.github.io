package com.healerjean.proj.shiro;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName AuthConfiguration
 * @Author TD
 * @Date 2019/6/3 20:11
 * @Description 认证属性
 */
@Configuration
@PropertySource(value = "classpath:shiro.properties", ignoreResourceNotFound = true, encoding = "UTF-8")
@Data
public class AuthConfiguration {

    @Value("${hlj.auth.loginUrl:}")
    private String loginUrl;
    @Value("${hlj.auth.clientName:}")
    private String clientName;
    @Value("${hlj.sessionExpire:1800}")
    private Integer sessionExpire;
    @Value("${hlj.sessionUserExpire:1800}")
    private Integer sessionUserExpire;
    @Value("${hlj.auth.redisTempleName:}")
    private String redisTempleName;
    @Value("${hlj.auth.anonPath: ,,}")
    private String[] anonPath;
}
