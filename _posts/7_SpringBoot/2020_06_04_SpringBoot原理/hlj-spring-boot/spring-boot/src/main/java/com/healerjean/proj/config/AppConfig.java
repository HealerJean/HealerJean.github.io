package com.healerjean.proj.config;

import com.healerjean.proj.bean.AppBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HealerJean
 * @ClassName AppConfig
 * @date 2020/6/4  17:17.
 * @Description
 */
@Configuration(proxyBeanMethods = false)
public class AppConfig {


    @Bean
    public AppBean appBean() {
        return new AppBean();
    }

}
