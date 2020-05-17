package com.healerjean.proj.schedule.service.impl;

import com.healerjean.proj.schedule.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author HealerJean
 * @ClassName SchedulerServiceImpl
 * @date 2020/5/15  17:41.
 * @Description
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {


    @Resource
    private Scheduler scheduler;


    /**
     * 启动任务
     *
     * @param cron      Cron 表达式
     * @param name      任务名称
     * @param className 任务类
     * @param jobDesc   任务描述
     */
    @Override
    public void startJob( String name, String className, String cron,String jobDesc) {
        try {
            Class jobClass = Class.forName(className);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name).withDescription(jobDesc).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            log.info("定时器服务--------启动任务--------任务名称：{}, 任务类：{}，corn表达式", name, className, cron);
        } catch (SchedulerException e) {
            log.error("定时器服务--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error("定时器服务--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 重置任务
     *
     * @param name 任务名称
     * @param cron Cron 表达式
     */
    @Override
    public void resetJob(String name, String cron) {
        try {
            JobKey jobKey = new JobKey(name);
            if (scheduler.checkExists(jobKey)) {
                TriggerKey triggerKey = TriggerKey.triggerKey(name);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
                log.info("定时器服务--------重置任务--------任务名称：{}", name);
            }
            throw new RuntimeException("定时器服务--------重置任务--------任务" + name + "不存在");
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 暂停任务
     *
     * @param name 任务名称
     */
    @Override
    public void pauseJob(String name) {
        try {
            JobKey jobKey = new JobKey(name);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                log.info("定时器服务--------暂停任务--------任务名称：{}", name);
            }
        } catch (SchedulerException e) {
            log.error("定时器服务--------暂停任务失败--------任务名称：" + name + "", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 删除任务
     */
    @Override
    public void deleteJob(String name) {
        try {
            if (scheduler.checkExists(new JobKey(name))) {
                scheduler.deleteJob(new JobKey(name));
                log.info("定时器服务--------删除任务--------任务名称：{}", name);
            }
        } catch (SchedulerException e) {
            log.error("定时器服务--------删除任务失败--------任务名称：" + name + "", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 暂停重启
     */
    @Override
    public void resumeJob(String name) {
        try {
            if (scheduler.checkExists(new JobKey(name))) {
                scheduler.resumeJob(new JobKey(name));
                log.info("定时器服务--------暂停重启任务--------任务名称：{}", name);
            }
        } catch (SchedulerException e) {
            log.error("定时器服务--------暂停重启任务失败--------任务名称：" + name + "", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 获取所有的任务 的 JobKey
     */
    @Override
    public Set<JobKey> currentJobs() {
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            return scheduler.getJobKeys(matcher);
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 获取任务详情 JobDetail
     */
    @Override
    public JobDetail getJobDetail(String name) {
        try {
            JobKey jobKey = new JobKey(name);
            return scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取触发器 Trigger
     *
     * @return
     */
    @Override
    public Trigger getJobTrigger(String name) {
        try {
            TriggerKey triggerKey = new TriggerKey(name);
            return scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取触发器 任务的执行状态
     */
    @Override
    public Trigger.TriggerState getTriggerState(String name ) {
        try {
            TriggerKey triggerKey = new TriggerKey(name);
            return scheduler.getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }


    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    @Override
    public void startAllJob() {
        try {
            scheduler.start();
            log.info("定时器服务--------开启定时器}");
        } catch (SchedulerException e) {
            log.error("定时器服务--------开启定时器失败", e);
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
            log.info("定时器服务--------关闭定时器}");
        } catch (SchedulerException e) {
            log.error("定时器服务--------关闭定时器失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}
