package com.healerjean.proj;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@EnableDubbo(scanBasePackages = "com.healerjean.proj.service")
@PropertySource("classpath:dubbo.properties")
@SpringBootApplication
public class ServerProvider_2001_Application {

    public static void main(String[] args) {
        SpringApplication.run(ServerProvider_2001_Application.class, args);
    }

}
