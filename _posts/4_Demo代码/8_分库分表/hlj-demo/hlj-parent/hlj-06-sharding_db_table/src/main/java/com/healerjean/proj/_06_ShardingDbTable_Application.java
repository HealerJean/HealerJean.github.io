package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class _06_ShardingDbTable_Application {

    public static void main(String[] args) {
        SpringApplication.run(_06_ShardingDbTable_Application.class, args);
    }

}
