package com.duodian.admore.config;

import com.duodian.admore.api.interceptor.APISessionInterceptor;
import com.duodian.admore.config.interceptors.PermissionInterceptor;
import com.duodian.admore.config.interceptors.SessionNewInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人： j.sh
 * 创建时间： 2016/9/13
 * version：1.0.0
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter{

    @Resource
    private PermissionInterceptor permissionInterceptor;
    @Resource
    private APISessionInterceptor apiSessionInterceptor;
    @Resource
    private SessionNewInterceptor sessionNewInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionNewInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/api/**")
                .excludePathPatterns("/register/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/public/**")
                .excludePathPatterns("/fan/**")
                .excludePathPatterns("/custom/**")
                .excludePathPatterns("/templet/**")
                .excludePathPatterns("/workflow/ding/**")
                ;

        registry.addInterceptor(permissionInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/api/**")
                .excludePathPatterns("/register/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/public/**")
                .excludePathPatterns("/fan/**")
                .excludePathPatterns("/custom/**")
                .excludePathPatterns("/templet/**")
                .excludePathPatterns("/workflow/ding/**")
                ;

        registry.addInterceptor(apiSessionInterceptor).addPathPatterns("/api/**");
        super.addInterceptors(registry);
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.parseMediaType("text/html"));
        mediaTypes.add(MediaType.parseMediaType("application/json"));
        mediaTypes.add(MediaType.parseMediaType("application/xhtml+xml"));
        mediaTypes.add(MediaType.parseMediaType("application/xml"));
        mediaTypes.add(MediaType.parseMediaType("application/xhtml"));

        jackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);
        converters.add(jackson2HttpMessageConverter);
    }
}
