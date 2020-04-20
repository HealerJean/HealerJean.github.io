package com.hlj.proj.config;

import com.hlj.proj.config.filter.Filter_1;
import com.hlj.proj.config.filter.Filter_2;
import com.hlj.proj.config.interceptor.Interceptor_1;
import com.hlj.proj.config.interceptor.Interceptor_2;
import com.hlj.proj.config.interceptor.UrlInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;

/**
 * 拦截器
 * 作者：  HealerJean
 * 日期:  2018/11/8 下午3:57.
 * 类描述:
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private UrlInterceptor urlInterceptor;

    @Resource
    private Interceptor_1 interceptor_1;
    @Resource
    private Interceptor_2 interceptor_2;
    /**
     *  swagger增加url映射
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

        registry.addInterceptor(interceptor_1)
                .addPathPatterns("/**")
                .excludePathPatterns("/develop/swagger/**");

        registry.addInterceptor(interceptor_2)
                .addPathPatterns("/**")
                .excludePathPatterns("/develop/swagger/**");

    }



    @Bean
    public FilterRegistrationBean filter1() {
        FilterRegistrationBean fitler = new FilterRegistrationBean();
        fitler.setFilter(new Filter_1());
        fitler.addUrlPatterns("/hlj/*");
        fitler.setName("filter_1");
        fitler.setDispatcherTypes(DispatcherType.REQUEST);
        return fitler;
    }

    @Bean
    public FilterRegistrationBean filter2() {
        FilterRegistrationBean fitler = new FilterRegistrationBean();
        fitler.setFilter(new Filter_2());
        fitler.addUrlPatterns("/hlj/*");
        fitler.setName("filter_2");
        fitler.setDispatcherTypes(DispatcherType.REQUEST);
        return fitler;
    }

}
