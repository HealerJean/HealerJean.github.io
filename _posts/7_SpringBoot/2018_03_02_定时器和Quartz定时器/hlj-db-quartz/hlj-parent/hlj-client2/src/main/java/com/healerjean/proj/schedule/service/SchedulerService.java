package com.healerjean.proj.schedule.service;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.Set;

/**
 * @author HealerJean
 * @ClassName SchedulerService
 * @date 2020/5/15  17:40.
 * @Description
 */
public interface SchedulerService {

    /**
     * 启动任务
     *
     * @param cron      Cron 表达式
     * @param name      任务名称
     * @param className 任务类
     */
    void startJob( String name, String className, String cron,String jobDesc);


    /**
     * 重置任务
     *
     * @param name 任务名称
     * @param cron Cron 表达式
     */
    void resetJob(String name, String cron);


    /**
     * 暂停任务
     */
    void pauseJob(String name);


    /**
     * 删除任务
     */
    void deleteJob(String name);


    /**
     * 暂停重启
     */
    void resumeJob(String name);


    /**
     * 获取所有的任务 的 JobKey
     */
    Set<JobKey> currentJobs();


    /**
     * 获取任务详情 JobDetail
     */
    JobDetail getJobDetail(String name);

    /**
     * 获取触发器 Trigger
     */
    Trigger getJobTrigger(String name);


    /**
     * 获取触发器 任务的执行状态
     */
    Trigger.TriggerState getTriggerState(String name);

    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    void startAllJob();

    /**
     * 关闭定时器，则所有任务不能执行和创建
     */
    void shutdown();
}
