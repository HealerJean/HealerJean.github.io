package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableDiscoveryClient//服务发现
@SpringBootApplication
public class Config_5001_Application {

    public static void main(String[] args) {
        SpringApplication.run(Config_5001_Application.class, args);
    }

}
