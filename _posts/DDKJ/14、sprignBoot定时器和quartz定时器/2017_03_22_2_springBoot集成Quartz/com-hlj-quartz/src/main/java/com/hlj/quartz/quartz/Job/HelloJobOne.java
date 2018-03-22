package com.hlj.quartz.quartz.Job;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * @Description 任务类.
 * @Author HealerJean
 * @Date 2018/3/22  下午4:17.
 */


public class HelloJobOne implements Job{

        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("一号任务"+new Date());

        }

}