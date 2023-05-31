package com.healerjean.proj;

import com.healerjean.proj.starter.annotation.EnableLogRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableLogRecord(tenant = "com.healerjean.test")
@SpringBootApplication
public class HljClientApplication {


    public static void main(String[] args) {
        SpringApplication.run(HljClientApplication.class, args);
    }

}
