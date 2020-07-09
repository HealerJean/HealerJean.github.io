package com.healerjean.proj;

import com.healerjean.proj.bean.AppBean;
import com.healerjean.proj.config.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
@SpringBootApplication
// @ComponentScan(basePackages = {"com.healerjean.prob"})
public class SpringBoot_Application {

    public static void main(String[] args) {
        log.info("SpringBoot_Application--------run");
        SpringApplication.run(SpringBoot_Application.class, args);
    }

}
