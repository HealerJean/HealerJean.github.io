package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ConfigClient_6001_Application {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClient_6001_Application.class, args);
    }

}
