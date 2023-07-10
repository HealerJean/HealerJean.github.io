package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * TomcatLauncher
 *
 * @author zhangyujin
 * @date 2023/6/14  15:30
 */
@EnableTransactionManagement
@SpringBootApplication
public class TomcatLauncher {


    public static void main(String[] args) {
        SpringApplication.run(TomcatLauncher.class, args);
    }

}
