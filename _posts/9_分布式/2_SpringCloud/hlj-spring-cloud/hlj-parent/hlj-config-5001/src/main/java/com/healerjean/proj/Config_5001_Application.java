package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy //开启zuul 的API网关服务功能
@SpringCloudApplication
public class Config_5001_Application {

    public static void main(String[] args) {
        SpringApplication.run(Config_5001_Application.class, args);
    }


}
