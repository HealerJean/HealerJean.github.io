package com.healerjean.proj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HealerJean
 * @ClassName DemoController
 * @date 2020/5/9  14:29.
 * @Description
 */
@RestController
@RequestMapping("demo")
public class DemoController {

    @GetMapping("connect")
    public String connect() {
        return "success";
    }
}
