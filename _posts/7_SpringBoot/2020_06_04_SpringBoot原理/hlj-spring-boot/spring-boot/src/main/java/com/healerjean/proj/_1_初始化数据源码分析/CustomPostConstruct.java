package com.healerjean.proj._1_初始化数据源码分析;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author HealerJean
 * @ClassName CustomPoststruct
 * @date 2020/6/17  14:49.
 * @Description
 */
@Component
@Slf4j
public class CustomPostConstruct {

    @PostConstruct
    public void init() {
        log.info("CustomPostConstruct--------@PostConstruct");
    }
}


