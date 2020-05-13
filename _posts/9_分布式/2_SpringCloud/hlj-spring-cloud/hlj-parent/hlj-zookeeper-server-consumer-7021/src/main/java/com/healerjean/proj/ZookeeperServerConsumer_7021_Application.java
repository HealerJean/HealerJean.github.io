package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients //开启声明式服务调用 feign(假装)
@SpringCloudApplication //可以取代下面三个
// @EnableCircuitBreaker //开启断路器功能
// @EnableDiscoveryClient //支持服务发现
// @SpringBootApplication
public class ZookeeperServerConsumer_7021_Application {

    //开启客户端负载均衡
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperServerConsumer_7021_Application.class, args);
    }

}
