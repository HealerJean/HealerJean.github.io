package com.hlj.quartz.quartz.Job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;


/**
 * @Description 任务类.
 * @Author HealerJean
 * @Date 2018/3/22  下午4:17.
 */


public class HelloJoTwo implements Job{

        public void execute(JobExecutionContext context) throws JobExecutionException {
            // 执行响应的任务.
            System.out.println("二号任务"+new Date());

        }

}