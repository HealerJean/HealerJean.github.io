package com.hlj.proj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
public class PointApplication implements CommandLineRunner {

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    public static void main(String[] args) {
        SpringApplication.run(PointApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(platformTransactionManager.getClass().getName());
    }
}
