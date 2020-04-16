package com.healerjean.proj.config;

import com.healerjean.proj.config.interceptor.UrlInterceptor;
import com.healerjean.proj.config.interceptor.SystemAuthTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

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
    @Resource
    private SystemAuthTokenInterceptor systemAuthTokenInterceptor ;

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

        /** 登陆的拦截器会拦截所有的，zull拦截固定路径  */
        registry.addInterceptor(systemAuthTokenInterceptor)
                .addPathPatterns("/api/provider/feign/zuul/**")
                .excludePathPatterns("/develop/swagger/**");
    }






}
