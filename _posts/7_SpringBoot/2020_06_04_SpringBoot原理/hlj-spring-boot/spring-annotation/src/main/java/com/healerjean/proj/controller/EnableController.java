package com.healerjean.proj.controller;

import com.custom.proj.service.CounterEnableService;
import com.custom.proj.service.LoggerEnableService;
import com.custom.proj.service.MonitorEnableService;
import com.custom.proj.service.inner.IronManService;
import com.custom.proj.service.inner.SpiderMainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("enable")
public class EnableController {

    @Resource
    private LoggerEnableService loggerEnableService;
    @Resource
    private CounterEnableService counterEnableService;
    @Resource
    private MonitorEnableService monitorEnableService;
    @Resource
    private SpiderMainService spiderMainService;
    @Resource
    private IronManService ironManService;

    @GetMapping("test")
    public String test() {
        loggerEnableService.saveLog("message");
        counterEnableService.add(1);
        monitorEnableService.saveMonitor();

        //configuration
        spiderMainService.spiderPrint();
        ironManService.print();
        return "ok";
    }
}
