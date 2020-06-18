package com.healerjean.proj.initdata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author HealerJean
 * @ClassName CustomApplicationRunner
 * @date 2020/6/17  14:44.
 * @Description
 */
@Order(1)
@Slf4j
@Component
public class CustomApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("CustomApplicationRunner implements ApplicationRunner--------run方法");
    }
}
