package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableConfigServer //添加分布式配置服务端支持
@EnableEurekaClient //添加服务发现
@SpringBootApplication
public class ConfigServer_5001_Application {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServer_5001_Application.class, args);
    }

}
