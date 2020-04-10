package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.ConsumeService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author HealerJean
 * @ClassName ConsumeServiceImpl
 * @date 2020/4/9  11:32.
 * @Description
 */
@Service
public class ConsumeServiceImpl implements ConsumeService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${hlj.server.providerName}")
    private String serverProviderName;

    @Override
    @HystrixCommand(fallbackMethod = "fallBack")
    public String hystrixFallBack() {
        return restTemplate.getForEntity("http://" + serverProviderName + "/api/provider/connect/", String.class).getBody();
        // int i = 1/0;
        // return "success";
    }

    public String fallBack() {
        return "hystrixFallBack 方法不可用，服务降级";
    }

}
