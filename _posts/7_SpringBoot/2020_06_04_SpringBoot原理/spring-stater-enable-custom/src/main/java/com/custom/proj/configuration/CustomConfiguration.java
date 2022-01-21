package com.custom.proj.configuration;

import com.custom.proj.configuration.service.IronManService;
import com.custom.proj.configuration.service.SpiderMainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangyujin
 * @date 2022/1/21  5:16 下午.
 * @description
 */
//建议加上@Configuration
@Configuration
public class CustomConfiguration {

    @Bean
    public IronManService ironManService(){
        return new IronManService();
    }

    @Bean
    public SpiderMainService spiderMainService(){
        return new SpiderMainService();
    }
}
