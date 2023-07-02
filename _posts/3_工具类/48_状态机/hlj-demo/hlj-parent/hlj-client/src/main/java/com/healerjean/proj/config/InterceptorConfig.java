package com.healerjean.proj.config;


import com.healerjean.proj.config.filter.Log4j2ReqUidFilter;
import com.healerjean.proj.config.interceptor.UrlInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;

/**
 * InterceptorConfig
 *
 * @author zhangyujin
 * @date 2023/6/14  19:55
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * urlInterceptor
     */
    @Resource
    private UrlInterceptor urlInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/develop/swagger/**");
    }

    @Bean
    public FilterRegistrationBean<Log4j2ReqUidFilter> log4j2ReqUidFilter() {
        FilterRegistrationBean<Log4j2ReqUidFilter> fitler = new FilterRegistrationBean<>();
        fitler.setFilter(new Log4j2ReqUidFilter());
        fitler.addUrlPatterns("/*");
        fitler.setName("log4j2ReqUidFilter");
        fitler.setDispatcherTypes(DispatcherType.REQUEST);
        return fitler;
    }

}
