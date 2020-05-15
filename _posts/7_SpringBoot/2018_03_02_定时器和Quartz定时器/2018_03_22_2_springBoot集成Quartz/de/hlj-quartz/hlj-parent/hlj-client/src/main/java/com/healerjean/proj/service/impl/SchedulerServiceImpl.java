package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HealerJean
 * @ClassName QuartzServiceImpl
 * @date 2020/5/15  12:27.
 * @Description
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Resource
    private Scheduler scheduler;

    /**
     * 开启定时器
     *
     * @param time         Cron 表达式
     * @param name         任务名称，
     * @param group        任务分组
     * @param jobClassName 任务类
     */
    @Override
    public void startJob(String cron, String name, String group, String jobClassName) {
        try {
            Class jobClass = Class.forName(jobClassName);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            log.info("quartz定时器--------启动任务--------任务名称：{}, 任务分组：{}", name, group);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error("quartz定时器--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 暂停任务
     *
     * @param name  任务名称
     * @param group 任务分组
     */
    @Override
    public void pauseJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.pauseJob(jobKey);
            log.info("quartz定时器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------暂停任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 继续任务：暂停中的任务
     * 注意：shutdown关闭了，或者删除了就不能重启了
     */
    @Override
    public void resumeJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.resumeJob(jobKey);
            log.info("quartz定时器--------继续任务--------任务名称：{}, 任务分组：{}", name, group);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------继续任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 删除任务
     */
    @Override
    public void deleteJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.deleteJob(jobKey);
            log.info("quartz定时器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------删除任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    @Override
    public void startAllJob() {
        try {
            scheduler.start();
            log.info("quartz定时器--------开启定时器}");
        } catch (SchedulerException e) {
            log.error("quartz定时器--------开启定时器失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 关闭定时器，则所有任务不能执行和创建
     */
    @Override
    public void shutdown() {
        try {
            scheduler.shutdown();
            log.info("quartz定时器--------关闭定时器}");
        } catch (SchedulerException e) {
            log.error("quartz定时器--------关闭定时器失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
