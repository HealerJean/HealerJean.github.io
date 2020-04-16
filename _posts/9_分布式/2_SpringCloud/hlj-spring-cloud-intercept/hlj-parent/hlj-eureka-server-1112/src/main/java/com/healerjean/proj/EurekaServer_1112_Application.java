package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//开启Eureka服务注册中心的支持,用来供给其他应用进行对话
@EnableEurekaServer
@SpringBootApplication
public class EurekaServer_1112_Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServer_1112_Application.class, args);
    }

}
