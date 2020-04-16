package com.healerjean.proj.service;

import com.healerjean.proj.config.interceptor.ZuulFeignProviderInterceptorConfig;
import com.healerjean.proj.service.impl.ConsumerFeignServerServiceFallBack;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author HealerJean
 * @ClassName ConsumerFeignServerServiceImpl
 * @date 2020/4/14  17:34.
 * @Description , fallback = ConsumerFeignServerServiceFallBack.class
 */
@FeignClient(name = "HLJ-SERVER-PROVIDER", //服务提供者应用名
        fallback = ConsumerFeignServerServiceFallBack.class,
        configuration = ZuulFeignProviderInterceptorConfig.class)
public interface ConsumerFeignServerService extends FeignServerService {

}
