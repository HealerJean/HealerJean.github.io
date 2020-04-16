package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy //开启zuul 的API网关服务功能
@SpringCloudApplication
public class ApiGateway_4001_Application {

    public static void main(String[] args) {
        SpringApplication.run(ApiGateway_4001_Application.class, args);
    }


}
