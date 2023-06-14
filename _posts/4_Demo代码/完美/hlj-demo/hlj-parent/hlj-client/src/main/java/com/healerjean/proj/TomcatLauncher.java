package com.healerjean.proj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * TomcatLauncher
 *
 * @author zhangyujin
 * @date 2023/6/14  15:30
 */
@MapperScan("com.healerjean.proj.data.mapper")
@SpringBootApplication
public class TomcatLauncher {


    public static void main(String[] args) {
        SpringApplication.run(TomcatLauncher.class, args);
    }

}
