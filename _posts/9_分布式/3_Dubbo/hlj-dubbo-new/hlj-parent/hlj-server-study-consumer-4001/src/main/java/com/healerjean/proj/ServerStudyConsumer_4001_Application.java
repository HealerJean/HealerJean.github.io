package com.healerjean.proj;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@EnableDubbo(scanBasePackages = "com.healerjean.proj.service")
@PropertySource("classpath:dubbo.properties")
@SpringBootApplication
public class ServerStudyConsumer_4001_Application {


    public static void main(String[] args) {
        SpringApplication.run(ServerStudyConsumer_4001_Application.class, args);
    }

}
