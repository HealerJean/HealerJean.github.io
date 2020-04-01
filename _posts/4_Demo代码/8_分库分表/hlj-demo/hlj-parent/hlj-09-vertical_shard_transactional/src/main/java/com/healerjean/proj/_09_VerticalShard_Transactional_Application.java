package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication(exclude = JtaAutoConfiguration.class)
public class _09_VerticalShard_Transactional_Application {

    public static void main(String[] args) {
        SpringApplication.run(_09_VerticalShard_Transactional_Application.class, args);
    }
}
