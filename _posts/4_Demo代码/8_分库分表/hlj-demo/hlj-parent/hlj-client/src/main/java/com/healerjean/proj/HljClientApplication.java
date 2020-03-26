package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

@EnableTransactionManagement
@SpringBootApplication
public class HljClientApplication  {

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    public static void main(String[] args) {
        SpringApplication.run(HljClientApplication.class, args);
    }

}
