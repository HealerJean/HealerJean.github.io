package com.healerjean.proj.config;

import com.healerjean.proj.bean.AppBean;
import com.healerjean.proj.study.spi.Bumblebee;
import com.healerjean.proj.study.spi.Robot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author HealerJean
 * @ClassName Appconfig
 * @date 2020/6/24  18:48.
 * @Description
 */
@Configuration
public class Appconfig {

    @Resource(type = Bumblebee.class)
    private Robot robot ;

    @Bean
    public AppBean appBean(){
        System.out.println(robot);
        return new AppBean();
    }
}
