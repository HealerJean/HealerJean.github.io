package com.healerjean.proj.strata.web.controller;

import com.healerjean.proj.strata.client.order.api.OrderService;
import com.healerjean.proj.strata.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: Web Demo
 *
 * @author DongBoot
 */

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(@RequestParam(name = "name", defaultValue = "demo") String name) {
        return new Result<>("Hello World " + name);
    }


}
