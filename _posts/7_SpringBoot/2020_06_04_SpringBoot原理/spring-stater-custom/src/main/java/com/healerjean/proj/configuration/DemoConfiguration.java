package com.healerjean.proj.configuration;


import com.healerjean.proj.bean.DemoBean;
import com.healerjean.proj.properties.DemoPeroperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

//要求这个类是否在classpath中存在，如果存在，才会实例化一个Bean
@ConditionalOnClass(DemoBean.class)
//使 DemoPeroperties 被ioc容器注入
@EnableConfigurationProperties(DemoPeroperties.class)
public class DemoConfiguration {

    @Autowired
    private DemoPeroperties demoPeroperties;

    @Bean
    ////容器中如果没有DemoBean这个类,那么自动配置这个Hello
    @ConditionalOnMissingBean(DemoBean.class)
    public DemoBean demoBean() {
        DemoBean demoBean = new DemoBean();
        demoBean.setName(demoPeroperties.getName());
        demoBean.setAge(demoBean.getAge());
        return demoBean;
    }
}
