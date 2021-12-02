package com.hlj.proj.controller;

import com.hlj.proj.aspect.LogIndex;
import com.hlj.proj.bean.LogBean;
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
 */
@RestController
@RequestMapping(value = "hlj")
@Slf4j
public class Log4j2Controller {

    @LogIndex
    @GetMapping("log4j2")
    public String log4j2(LogBean logBean) {
        log.debug("debug日志================{}", logBean);
        log.info("info日志=================={}", logBean);
        log.warn("warn日志=================={}", logBean);
        log.error("error日志================{}", logBean);
        log.info("{}", 1/0);
        return "日志处理成功";
    }


}
