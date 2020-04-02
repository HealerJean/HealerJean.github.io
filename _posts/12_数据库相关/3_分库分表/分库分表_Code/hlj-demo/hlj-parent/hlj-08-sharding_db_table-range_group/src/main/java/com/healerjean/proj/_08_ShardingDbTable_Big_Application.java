package com.healerjean.proj;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

@EnableTransactionManagement
@SpringBootApplication
public class _08_ShardingDbTable_Big_Application implements CommandLineRunner {

    @Resource
    private PlatformTransactionManager platformTransactionManager;

    public static void main(String[] args) {
        SpringApplication.run(_08_ShardingDbTable_Big_Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(platformTransactionManager.getClass().getName());
    }
}
