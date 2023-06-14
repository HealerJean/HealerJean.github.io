package com.healerjean.proj.config;


import com.healerjean.proj.config.interceptor.UrlInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

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


    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //     registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
    //     registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    //     registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    // }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/develop/swagger/**");
    }


}
