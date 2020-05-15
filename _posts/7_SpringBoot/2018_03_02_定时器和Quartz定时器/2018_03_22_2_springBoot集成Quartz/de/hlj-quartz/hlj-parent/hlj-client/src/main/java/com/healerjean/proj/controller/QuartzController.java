package com.healerjean.proj.controller;

import com.healerjean.proj.common.ResponseBean;
import com.healerjean.proj.service.DemoService;
import com.healerjean.proj.service.SchedulerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  下午4:47.
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "quartz控制器")
@RequestMapping("hlj/quartz")
@Slf4j
@RestController
public class QuartzController {


    @Autowired
    private DemoService demoService;
    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("oneJob")
    public ResponseBean oneJob() {
        demoService.oneJob();
        return ResponseBean.buildSuccess();
    }



    @GetMapping("startJob")
    public ResponseBean startJob(String time, String name, String group, String className) {
        log.info("quartz控制器--------开启任务--------任务名称：{}, 任务分组：{}", name, group);
        schedulerService.startJob(time, name, group, className);
        return ResponseBean.buildSuccess("已经开启任务");
    }

    @GetMapping("pauseJob")
    public ResponseBean pauseJob(String name, String group) {
        log.info("quartz控制器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
        schedulerService.pauseJob(name, group);
        return ResponseBean.buildSuccess("暂停任务");
    }


    @GetMapping("resumeJob")
    public ResponseBean resumeJob(String name, String group) {
        log.info("quartz控制器--------暂停任务");
        schedulerService.resumeJob(name, group);
        return ResponseBean.buildSuccess("暂停后继续任务");
    }

    @GetMapping("deleteJob")
    public ResponseBean deleteJob(String name, String group) {
        log.info("quartz控制器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
        schedulerService.deleteJob(name, group);
        return ResponseBean.buildSuccess("删除任务");
    }


    @GetMapping("startAllJob")
    public ResponseBean startAllJob() {
        log.info("quartz控制器--------启动定时器");
        schedulerService.startAllJob();
        return ResponseBean.buildSuccess("启动定时器");
    }

    @GetMapping("shutdown")
    public ResponseBean shutdown() {
        log.info("quartz控制器--------关闭定时器");
        schedulerService.shutdown();
        return ResponseBean.buildSuccess("关闭定时器");
    }

}
