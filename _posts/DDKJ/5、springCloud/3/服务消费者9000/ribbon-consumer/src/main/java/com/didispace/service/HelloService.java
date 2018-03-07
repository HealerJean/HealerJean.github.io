package com.didispace.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallBack")
    public String hello(){

        return restTemplate.getForEntity("http://HELLO-SERVICE/hello",String.class).getBody();
    }


    public String helloFallBack(){
        return "error";
    }
}
