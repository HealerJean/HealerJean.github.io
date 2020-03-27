package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Sharding JDBC 垂直拆分（不同的表在不同的库中）
 */
@EnableTransactionManagement
@SpringBootApplication
public class _02_VerticalShard_Application {

    public static void main(String[] args) {
        SpringApplication.run(_02_ReadWrite_VerticalShard_Application.class, args);
    }

}
