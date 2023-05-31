package com.hlj.proj.controller;

import com.hlj.proj.aspect.BusinessLog;
import com.hlj.proj.aspect.LogIndex;
import com.hlj.proj.bean.BusinessDTO;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangyujin
 * @date 2022/1/17  7:16 下午.
 * @description
 */
@RestController
@RequestMapping(value = "hlj/business/log")
@Slf4j
public class BusinessLogController {

    @BusinessLog(operator = "#bus.operator", action = "创建", bizNo = "#bus.orderId")
    @LogIndex
    @GetMapping("demo")
    public String add(BusinessDTO bus) {
        return JsonUtils.toJsonString(bus);
    }

}
