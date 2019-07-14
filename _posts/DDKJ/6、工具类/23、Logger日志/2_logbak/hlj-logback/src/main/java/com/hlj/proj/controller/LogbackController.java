package com.hlj.proj.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName LogbackController
 * @date 2019/7/12  15:12.
 * @Description logback 测试
 *
 */
@RestController
@RequestMapping(value = "hlj")
@Slf4j
public class LogbackController {

    @GetMapping("logback")
    public String logback() {
        log.debug("debug日志==================");
        log.info("info日志==================");
        log.warn("warn日志====================");
        log.error("error日志=====================");
        int i = 1 / 0;
        System.out.println(i);
        return "日志处理成功";
    }
}
