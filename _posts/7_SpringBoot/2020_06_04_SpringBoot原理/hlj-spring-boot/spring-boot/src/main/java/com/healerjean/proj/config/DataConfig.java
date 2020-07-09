package com.healerjean.proj.config;

import com.healerjean.proj.bean.AppBean;
import com.healerjean.proj.bean.DataBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class DataConfig implements ApplicationContextAware {

    private  ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Autowired
    private AppConfig appConfig;

    @Bean
    public DataBean dataBean() {
        log.info("{}", appConfig);
        AppConfig appConfig2 =   applicationContext.getBean(AppConfig.class);
        return new DataBean();
    }

}
