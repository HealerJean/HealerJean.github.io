package com.healerjean.proj.scheduler.service;


import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.Set;

/**
 * @author HealerJean
 * @ClassName QuartzService
 * @date 2020/5/15  12:27.
 * @Description
 */
public interface SchedulerService {

    /**
     * 启动任务
     *
     * @param name      任务名称，
     * @param group     任务分组
     * @param className 任务类
     * @param cron      Cron 表达式
     */
    void startJob(String name, String group, String className, String cron,  String jobDesc);

    /**
     * 重置任务
     */
    void resetJob(String name, String group, String cron);

    /**
     * 暂停任务
     *
     * @param name  任务名称
     * @param group 任务分组
     */
    void pauseJob(String name, String group);

    /**
     * 继续定时器任务：暂停中的任务
     * 注意：shutdown关闭了，或者删除了就不能重启了
     */
    void resumeJob(String name, String group);


    /**
     * 删除定时器任务
     */
    void deleteJob(String name, String group);


    /**
     * 获取所有的任务 的 JobKey
     */
    Set<JobKey> currentJobs();

    /**
     * 获取任务详情 JobDetail
     */
    JobDetail getJobDetail(String name, String group);

    /**
     * 获取触发器 Trigger
     */
    Trigger getJobTrigger(String name, String group);


    /**
     * 获取触发器 任务的执行状态
     */
    Trigger.TriggerState getTriggerState(String name, String group);

    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    void startAllJob();

    /**
     * 关闭定时器，则所有任务不能执行和创建
     */
    void shutdown();
}
