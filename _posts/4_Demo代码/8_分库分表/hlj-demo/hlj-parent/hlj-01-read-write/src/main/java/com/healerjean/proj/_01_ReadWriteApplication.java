package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class _01_ReadWriteApplication {

    public static void main(String[] args) {
        SpringApplication.run(_02_VerticalShard_Application.class, args);
    }

}
