package com.healerjean.proj.controller;

import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.dto.ScheduleJobDTO;
import com.healerjean.proj.schedule.service.SchedulerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
    private SchedulerService schedulerService;



    @GetMapping("startJob")
    public ResponseBean startJob( String name, String className, String cron, String jobDesc) {
        log.info("quartz控制器--------启动任务--------任务名称：{}, 任务类：{}，corn表达式", name, className, cron);
        schedulerService.startJob(name, className,cron, jobDesc);
        return ResponseBean.buildSuccess("已经开启任务");
    }

    @GetMapping("pauseJob")
    public ResponseBean pauseJob(String name ) {
        log.info("quartz控制器--------暂停任务--------任务名称：{}", name);
        schedulerService.pauseJob(name);
        return ResponseBean.buildSuccess("暂停任务");
    }


    @GetMapping("resumeJob")
    public ResponseBean resumeJob(String name ) {
        log.info("quartz控制器--------暂停任务");
        schedulerService.resumeJob(name);
        return ResponseBean.buildSuccess("暂停后继续任务");
    }

    @GetMapping("deleteJob")
    public ResponseBean deleteJob(String name) {
        log.info("quartz控制器--------删除任务--------任务名称：{}", name);
        schedulerService.deleteJob(name);
        return ResponseBean.buildSuccess("删除任务");
    }

    @GetMapping("currentJobs")
    public ResponseBean currentJobs() {
        log.info("quartz控制器--------获取所有的任务");
        Set<JobKey> jobKeys = schedulerService.currentJobs();
        List<ScheduleJobDTO> jobList = new ArrayList<>();

        for (JobKey jobKey : jobKeys){
            JobDetail jobDetail = schedulerService.getJobDetail(jobKey.getName());
            Trigger trigger = schedulerService.getJobTrigger(jobKey.getName());
            Trigger.TriggerState triggerState = schedulerService.getTriggerState(jobKey.getName());
            ScheduleJobDTO jobDTO = new ScheduleJobDTO();
            jobDTO.setJobName(jobKey.getName());
            jobDTO.setJobDesc(jobDetail.getDescription());
            jobDTO.setCron(((CronTrigger)trigger).getCronExpression());
            jobDTO.setJobClass(jobDetail.getJobClass().toString());
            jobDTO.setPreviousFireTime(trigger.getPreviousFireTime());
            jobDTO.setNextFireTime(trigger.getNextFireTime());
            jobDTO.setJobStatus(triggerState.name());
            jobList.add(jobDTO);
        }
        return ResponseBean.buildSuccess("获取所有的任务成功", jobList);
    }


    @GetMapping("getTriggerState")
    public ResponseBean getTriggerState(String name) {
        log.info("quartz控制器--------任务的执行状态--------任务名称：{}", name);
        return ResponseBean.buildSuccess("任务的执行状态",schedulerService.getTriggerState(name));
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
