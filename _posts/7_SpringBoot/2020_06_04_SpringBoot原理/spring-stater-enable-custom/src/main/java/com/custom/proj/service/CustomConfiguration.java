package com.custom.proj.service;

import com.custom.proj.service.inner.IronManService;
import com.custom.proj.service.inner.SpiderMainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangyujin
 * @date 2022/1/21  5:16 下午.
 * @description
 */
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
