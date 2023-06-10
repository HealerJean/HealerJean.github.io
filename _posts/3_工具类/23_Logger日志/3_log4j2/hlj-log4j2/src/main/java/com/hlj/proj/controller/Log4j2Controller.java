package com.hlj.proj.controller;

import com.hlj.proj.aspect.LogIndex;
import com.hlj.proj.bean.LogBean;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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


    @LogIndex(resFlag = false)
    @GetMapping("log4j2")
    public String log4j2(LogBean logBean) {
        log.debug("debug日志================{}", logBean);
        log.info("info日志=================={}", logBean);
        log.warn("warn日志=================={}", logBean);
        log.error("error日志================{}", logBean);
        // log.info("{}", 1/0);
        return "日志处理成功";
    }

    @LogIndex
    @GetMapping("many")
    public String log4j2(String name, Integer age) {
        return name + age;
    }


    @LogIndex
    @GetMapping("request")
    public String log4j2(HttpServletRequest request, HttpServletResponse response) {
        // log.info("info日志 json============= request:{}", JsonUtils.toJsonString(request));
        log.info("info日志 json============= response:{}", JsonUtils.toJsonString(response));
        return "日志处理成功";
    }

    @LogIndex
    @GetMapping("updateLevel")
    public String updateLevel(String name, String level) {
        Configurator.setLevel(name, Level.toLevel(level));
        log.debug("debug日志================{},level:{}", name,level);
        log.info("info日志=================={},level:{}", name,level);
        log.warn("warn日志=================={},level:{}", name,level);
        log.error("error日志================{},level:{}", name,level);
        return "修改日志级别成功";
    }




}
