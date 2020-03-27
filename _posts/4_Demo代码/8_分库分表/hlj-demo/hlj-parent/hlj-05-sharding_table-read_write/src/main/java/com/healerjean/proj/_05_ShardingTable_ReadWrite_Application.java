package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Boot版 Sharding JDBC 垂直拆分（不同的表在不同的库中）+ 读写分离案例
 */
@EnableTransactionManagement
@SpringBootApplication
public class _05_ShardingTable_ReadWrite_Application {

    public static void main(String[] args) {
        SpringApplication.run(_05_ShardingTable_ReadWrite_Application.class, args);
    }

}
