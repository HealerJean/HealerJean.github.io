package com.healerjean.proj.configuration;


import com.healerjean.proj.bean.NextBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//要求这个类是否在classpath中存在，如果存在，才会实例化一个Bean
@ConditionalOnClass(NextBean.class)
@Configuration
public class NextConfiguration {


    @Bean
    @ConditionalOnMissingBean(NextBean.class)
    public NextBean nextBean() {
        return new NextBean();
    }
}
