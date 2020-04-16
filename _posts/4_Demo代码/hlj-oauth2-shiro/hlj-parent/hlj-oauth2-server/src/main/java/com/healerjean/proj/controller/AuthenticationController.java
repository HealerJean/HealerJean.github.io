package com.healerjean.proj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/oauth2")
public class AuthenticationController {

    /**
     * 登录页面请求
     * 1、判断域名应该启用什么登录页
     */
    @GetMapping(value = "login")
    public void authorize(@RequestParam(value =  "redirectUri", required = true) String redirectUri){

    }

}
