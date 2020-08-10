package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.EnableZipkinServer;

// 开启 Zipkin Server 的功能
@EnableZipkinServer
//开启服务发现
@EnableDiscoveryClient
@SpringBootApplication
public class ZipkinServer_9001_Application {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinServer_9001_Application.class, args);
    }

}
