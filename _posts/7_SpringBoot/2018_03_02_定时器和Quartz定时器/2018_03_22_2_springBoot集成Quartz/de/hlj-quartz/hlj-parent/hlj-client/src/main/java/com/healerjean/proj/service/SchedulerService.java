package com.healerjean.proj.service;


/**
 * @author HealerJean
 * @ClassName QuartzService
 * @date 2020/5/15  12:27.
 * @Description
 */
public interface SchedulerService {

    /**
     * 开启定时器
     *
     * @param time         Cron 表达式
     * @param jobName      任务名称，
     * @param group        任务分组
     * @param jobClassName 任务类
     */
    void startJob(String time, String jobName, String group, String jobClassName);


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


    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    void startAllJob();

    /**
     * 关闭定时器，则所有任务不能执行和创建
     */
    void shutdown();
}
