package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@EnableRetry
@SpringBootApplication
public class HljClientApplication {


    public static void main(String[] args) {
        SpringApplication.run(HljClientApplication.class, args);
    }

}
