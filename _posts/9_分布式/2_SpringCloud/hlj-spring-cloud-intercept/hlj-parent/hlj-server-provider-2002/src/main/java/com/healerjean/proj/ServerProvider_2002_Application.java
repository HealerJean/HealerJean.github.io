package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

//支持服务发现
@EnableDiscoveryClient
@SpringBootApplication
public class ServerProvider_2002_Application {

    public static void main(String[] args) {
        SpringApplication.run(ServerProvider_2002_Application.class, args);
    }

}
