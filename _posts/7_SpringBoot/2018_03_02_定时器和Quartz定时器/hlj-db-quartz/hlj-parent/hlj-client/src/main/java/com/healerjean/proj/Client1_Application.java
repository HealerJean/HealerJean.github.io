package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = {"com.healerjean.proj.dao.repository"})
@EntityScan(basePackages = {"com.healerjean.proj"})
@EnableTransactionManagement
@SpringBootApplication
public class Client1_Application {

    public static void main(String[] args) {
        SpringApplication.run(Client1_Application.class, args);
    }

}
