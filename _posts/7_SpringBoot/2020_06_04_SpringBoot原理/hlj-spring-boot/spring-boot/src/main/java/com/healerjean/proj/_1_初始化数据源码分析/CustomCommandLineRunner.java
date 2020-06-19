package com.healerjean.proj._1_初始化数据源码分析;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author HealerJean
 * @ClassName CustomCommandLineRunner
 * @date 2020/6/17  14:45.
 * @Description
 */
@Slf4j
@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("CustomCommandLineRunner implements CommandLineRunner--------run方法");
    }
}
