package com.healerjean.proj.config;

import com.healerjean.proj.config.properties.DemoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @author HealerJean
 * @ClassName DemoPropertiesAutoConfiguration
 * @date 2020/6/11  10:18.
 * @Description
 */
@Configuration
@Slf4j
public class DemoPropertiesAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Autowired
    private DemoProperties demoProperties;

    @PostConstruct
    public void init() {
        log.info("配置文件属性--------{}", demoProperties);
        log.info("demo.name ==> {}" , environment.getProperty("demo.name"));
        log.info("demo.version ==> {}" , environment.getProperty("demo.version"));

    }

}
