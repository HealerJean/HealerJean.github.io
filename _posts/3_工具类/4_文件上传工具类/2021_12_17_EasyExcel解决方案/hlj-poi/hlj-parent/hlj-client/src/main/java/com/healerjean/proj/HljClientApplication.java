package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class HljClientApplication  {

    public static void main(String[] args) {
        SpringApplication.run(HljClientApplication.class, args);
    }

}
