package com.healerjean.proj.config;

import com.healerjean.proj.config.filter.Log4j2ReqUidFilter;
import com.healerjean.proj.config.interceptor.UrlInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;

/**
 * 拦截器
 * 作者：  HealerJean
 * 日期:  2018/11/8 下午3:57.
 * 类描述:
 */

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {


    @Resource
    private UrlInterceptor urlInterceptor;

    /**
     * swagger增加url映射
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/develop/swagger/**")
                .addResourceLocations("classpath:/swagger/dist/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/develop/swagger/**");
    }



    @Bean
    public FilterRegistrationBean log4j2ReqUidFilter() {
        FilterRegistrationBean fitler = new FilterRegistrationBean();
        fitler.setFilter(new Log4j2ReqUidFilter());
        fitler.addUrlPatterns("/*");
        fitler.setName("Log4j2ReqUidFilter");
        fitler.setDispatcherTypes(DispatcherType.REQUEST);
        return fitler;
    }


}
