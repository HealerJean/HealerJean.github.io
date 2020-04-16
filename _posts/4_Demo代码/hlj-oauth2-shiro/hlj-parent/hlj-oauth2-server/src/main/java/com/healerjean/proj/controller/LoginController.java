package com.healerjean.proj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("")
public class LoginController {



    /**
     * 用户登陆页面
     */
    @GetMapping("login")
    public String loginGet(@RequestParam(value = "redirect_uri", required = false) String redirectUri) {



        return "1";
    }

    /**
     * 用户登陆数据提交
     * 1.参数校验
     * 2.登陆信息校验
     * 3.登陆成功
     * a.登陆有重定向相关信息，则重定向到回调地址
     * b.登陆无重定向相关信息，则跳转到用户中心主页
     */
    @PostMapping("login")
    @ResponseBody
    public String loginPost(
            @RequestParam(value = "redirect_uri", required = false) String redirectUri,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) {
        return "token";
    }

}
