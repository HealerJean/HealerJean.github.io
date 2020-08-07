package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import zipkin.server.EnableZipkinServer;

// 开启 Zipkin Server 的功能
@EnableZipkinServer
//开启Eureka服务注册中心的支持,用来供给其他应用进行对话
@EnableEurekaServer
@SpringBootApplication
public class ZipkinServer_9001_Application {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinServer_9001_Application.class, args);
    }

}
