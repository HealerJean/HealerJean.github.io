package com.healerjean.proj.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

//一个配置类只配置@ConfigurationProperties注解，
// 而没有使用@Component，那么在IOC容器中是获取不到properties 配置文件转化的bean
// @ConfigurationProperties("demo")

@ComponentScan
@ConfigurationProperties("demo")
@Data
public class DemoProperties {

    private String name ;
}
