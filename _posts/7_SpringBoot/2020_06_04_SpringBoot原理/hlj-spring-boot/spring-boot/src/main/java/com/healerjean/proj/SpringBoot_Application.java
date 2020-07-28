package com.healerjean.proj;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
// @ComponentScan(basePackages = {"com.healerjean.prob"})
public class SpringBoot_Application {

    public static void main(String[] args) {
        log.info("SpringBoot_Application--------run");
        SpringApplication.run(SpringBoot_Application.class, args);
    }

}
