package com.hlj.proj.config;

import com.hlj.proj.config.filter.SpaceFilter;
import com.hlj.proj.config.filter.XssFilter;
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
    }


    /**过滤器配置 ，配置了过滤器会让swagger失效，所以我后来讲swagger的路径修改为了/develop/swagge  */
    // @Bean
    // public FilterRegistrationBean spaceParamsFilter() {
    //     FilterRegistrationBean fitler = new FilterRegistrationBean();
    //     fitler.setFilter(new SpaceParamsFilter());
    //     fitler.addUrlPatterns("/hlj/*");
    //     fitler.setName("SpaceParamsFilter");
    //     fitler.setDispatcherTypes(DispatcherType.REQUEST);
    //     return fitler;
    // }
    // @Bean
    // public FilterRegistrationBean spaceJsonFilter() {
    //     FilterRegistrationBean fitler = new FilterRegistrationBean();
    //     fitler.setFilter(new SpaceJsonFilter());
    //     fitler.addUrlPatterns("/hlj/*");
    //     fitler.setName("SpaceJsonFilter");
    //     fitler.setDispatcherTypes(DispatcherType.REQUEST);
    //     return fitler;
    // }

    @Bean
    public FilterRegistrationBean spaceFilter() {
        FilterRegistrationBean fitler = new FilterRegistrationBean();
        fitler.setFilter(new SpaceFilter());
        fitler.addUrlPatterns("/hlj/*");
        fitler.setName("SpaceFilter");
        fitler.setDispatcherTypes(DispatcherType.REQUEST);
        return fitler;
    }

    @Bean
    public FilterRegistrationBean xssSqlFilter() {
        FilterRegistrationBean fitler = new FilterRegistrationBean();
        fitler.setFilter(new XssFilter());
        fitler.addUrlPatterns("/hlj/*");
        fitler.setName("XssFilter");
        fitler.setDispatcherTypes(DispatcherType.REQUEST);
        return fitler;
    }
}
