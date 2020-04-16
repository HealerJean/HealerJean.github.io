package com.healerjean.proj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/")
public class LogoutController {

    /**
     * 用户登出
     */
    @GetMapping(value = "logout")
    @ResponseBody
    public String logout() {
        return "";
    }

}
