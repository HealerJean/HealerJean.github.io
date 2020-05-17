package com.healerjean.proj.scheduler.service.impl;

import com.healerjean.proj.scheduler.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

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
     * 启动任务
     *
     * @param name      任务名称，
     * @param group     任务分组
     * @param className 任务类
     * @param cron      Cron 表达式
     */
    @Override
    public void startJob(String name, String group, String className, String cron,  String jobDesc) {
        try {
            Class jobClass = Class.forName(className);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).withDescription(jobDesc).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            log.info("quartz定时器--------启动任务--------任务名称：{}, 任务分组：{}, 任务类：{}，corn表达式", name, group, className, cron);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error("quartz定时器--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 重置任务
     */
    @Override
    public void resetJob(String name, String group, String cron) {
        try {
            JobKey jobKey = new JobKey(name, group);
            if (scheduler.checkExists(jobKey)) {
                TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
                log.info("quartz定时器--------重置任务--------任务名称：{},任务分组：{}", name, group);
            }
            throw new RuntimeException("quartz定时器--------重置任务--------任务" + name + "不存在");
        } catch (SchedulerException e) {
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
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                log.info("quartz定时器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
            }
        } catch (SchedulerException e) {
            log.error("quartz定时器--------暂停任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 暂停重启任务：暂停中的任务
     * 注意：shutdown关闭了，或者删除了就不能重启了
     */
    @Override
    public void resumeJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
                log.info("quartz定时器--------暂停重启任务--------任务名称：{}, 任务分组：{}", name, group);
            }
        } catch (SchedulerException e) {
            log.error("quartz定时器--------暂停重启任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
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
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
                log.info("quartz定时器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
            }
        } catch (SchedulerException e) {
            log.error("quartz定时器--------删除任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
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
    public JobDetail getJobDetail(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
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
    public Trigger getJobTrigger(String name, String group) {
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            return scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }


    /**
     * 获取触发器 任务的执行状态
     */
    @Override
    public Trigger.TriggerState getTriggerState(String name, String group) {
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
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
