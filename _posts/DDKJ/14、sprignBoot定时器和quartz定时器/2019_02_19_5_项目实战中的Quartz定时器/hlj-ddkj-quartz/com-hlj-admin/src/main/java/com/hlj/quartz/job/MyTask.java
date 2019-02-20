package com.hlj.quartz.job;

import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/23  下午4:19.
 */

@Component
public class MyTask implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {

            //可以通过context拿到执行当前任务的quartz中的很多信息，如当前是哪个trigger在执行该任务
            CronTrigger trigger = (CronTrigger) context.getTrigger();

            TriggerKey triggerKey =trigger.getKey() ;
            String corn = trigger.getCronExpression();
            System.out.println("任务规则 :"+corn);

            System.out.println("triggerKey  Name:"+triggerKey.getName()); //t_jobId
            System.out.println("triggerKey group:"+triggerKey.getGroup()); //t_jobId

            System.out.println("----------------------");
            JobKey jobKey =   context.getJobDetail().getKey() ;
            System.out.println("jobKey getName"+jobKey.getName()); //jobId
            System.out.println("jobKey getGroup"+jobKey.getGroup()); //DEFAULT

            Scheduler scheduler =  context.getScheduler();
            System.out.println("scheduler.getCalendarNames()"+scheduler.getCalendarNames());

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            System.out.println(jobDetail.getJobClass().toString());

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("执行任务中");
    }
}