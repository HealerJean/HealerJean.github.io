package com.healerjean.proj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RefreshScope //`spring-cloud` 实现更新配置不用重启服务**
@RestController
@RequestMapping("api/config/client")
public class ConfigClientController {

    @Value("${from}")
    private String from;

    @GetMapping("fromValue")
    public String from() {
        return this.from;
    }

}
