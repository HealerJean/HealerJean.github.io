package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer //添加分布式配置服务端支持
@SpringBootApplication
public class Config_5001_Application {

    public static void main(String[] args) {
        SpringApplication.run(Config_5001_Application.class, args);
    }

}
