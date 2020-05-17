package com.healerjean.proj.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

@Slf4j
public class OneJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
       log.info("quartz任务--------OneJob--------开始执行：执行事件：{}", new Date());
    }

}
