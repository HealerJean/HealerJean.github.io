package com.healerjean.proj.controller;

import com.custom.proj.configuration.service.IronManService;
import com.custom.proj.configuration.service.SpiderMainService;
import com.custom.proj.register.service.MonitorEnableBService;
import com.custom.proj.selector.service.CounterEnableAService;
import com.custom.proj.service.LoggerEnableService;
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
    private CounterEnableAService counterEnableAService;
    // @Resource
    // private CounterEnableBService counterEnableBService;
    // @Resource
    // private MonitorEnableAService monitorEnableAService;
    @Resource
    private MonitorEnableBService monitorEnableBService;
    @Resource
    private SpiderMainService spiderMainService;
    @Resource
    private IronManService ironManService;

    @GetMapping("test")
    public String test() {
        loggerEnableService.saveLog("message");

        counterEnableAService.add(1);
        // counterEnableBService.add(1);

        // monitorEnableAService.saveMonitor();
        monitorEnableBService.saveMonitor();

        //configuration
        spiderMainService.spiderPrint();
        ironManService.print();
        return "ok";
    }
}
