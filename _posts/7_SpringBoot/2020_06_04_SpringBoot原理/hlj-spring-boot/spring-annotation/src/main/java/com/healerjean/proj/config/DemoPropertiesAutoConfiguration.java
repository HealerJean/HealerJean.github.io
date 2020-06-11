package com.healerjean.proj.config;

import com.healerjean.proj.config.properties.DemoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author HealerJean
 * @ClassName DemoPropertiesAutoConfiguration
 * @date 2020/6/11  10:18.
 * @Description
 */
@Configuration
// @EnableConfigurationProperties(DemoProperties.class)
@Slf4j
public class DemoPropertiesAutoConfiguration {

    @Autowired
    private DemoProperties demoProperties;

    @PostConstruct
    public void init() {
        log.info("配置文件属性--------{}", demoProperties);
    }

}
