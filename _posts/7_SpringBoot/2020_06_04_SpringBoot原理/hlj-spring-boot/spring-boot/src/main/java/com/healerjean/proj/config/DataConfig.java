package com.healerjean.proj.config;

import com.healerjean.proj.bean.AppBean;
import com.healerjean.proj.bean.DataBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HealerJean
 * @ClassName DataConfit
 * @date 2020/6/4  18:58.
 * @Description
 */
@Configuration
@Slf4j
public class DataConfig {

    @Autowired
    private AppConfig appConfig;

    @Bean
    public DataBean dataBean() {
        log.info("{}", appConfig);
        return new DataBean();
    }

}
