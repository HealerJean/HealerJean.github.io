package com.healerjean.proj.config.interceptor;

import com.healerjean.proj.util.PropertiesUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author HealerJean
 * @ClassName ZuulFeignInterceptor
 * @date 2020/4/16  14:48.
 * @Description
 */
@Configuration
@Slf4j
public class ZuulFeignProviderInterceptorConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        requestTemplate.header("systemAuthToken", PropertiesUtil.getProperty("systemAuthToken.provider"));


        //healer重写
        // ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
        //         .getRequestAttributes();
        // HttpServletRequest request = attributes.getRequest();
        // Enumeration<String> headerNames = request.getHeaderNames();
        // if (headerNames != null) {
        //     while (headerNames.hasMoreElements()) {
        //         String name = headerNames.nextElement();
        //         String values = request.getHeader("Ttoken");
        //         requestTemplate.header("Ttoken", values);
        //     }
        // }
    }
}
