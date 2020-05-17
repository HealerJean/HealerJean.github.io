package com.healerjean.proj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author HealerJean
 * @ClassName HomeController
 * @date 2020-05-17  15:56.
 * @Description
 */
@Controller
@RequestMapping("")
public class HomeController {

    @RequestMapping(value = {"main", ""})
    public String main() {
        return "main.ftl";
    }

}
