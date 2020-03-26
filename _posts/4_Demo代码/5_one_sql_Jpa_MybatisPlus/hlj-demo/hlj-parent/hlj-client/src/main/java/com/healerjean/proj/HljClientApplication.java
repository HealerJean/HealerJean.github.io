package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

@EnableJpaRepositories(basePackages = {"com.healerjean.proj.dao.repository"})
@EntityScan(basePackages = {"com.healerjean.proj"})
@EnableTransactionManagement
@SpringBootApplication
public class HljClientApplication  {

    public static void main(String[] args) {
        SpringApplication.run(HljClientApplication.class, args);
    }

}
