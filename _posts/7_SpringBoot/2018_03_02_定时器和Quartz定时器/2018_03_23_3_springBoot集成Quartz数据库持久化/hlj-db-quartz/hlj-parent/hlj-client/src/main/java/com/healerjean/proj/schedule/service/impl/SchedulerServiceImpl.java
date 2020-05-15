package com.healerjean.proj.schedule.service.impl;

import com.healerjean.proj.schedule.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
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

    private static final  String TRIGGER_PERFIX = "t_";

    @Resource
    private Scheduler scheduler;

    public  void startJob(String cron, String name, String jobClassName){
        try {
            Class jobClass = Class.forName(jobClassName);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name).build();
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


    public void resetJob(String jobId,String corn) {
        try {
            if (scheduler.checkExists(new JobKey(jobId))){
                JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobId));
                TriggerKey triggerKey = TriggerKey.triggerKey(TRIGGER_PERFIX+jobDetail.getKey().getName(), jobDetail.getKey().getGroup());
                //获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(corn);
                //按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey,trigger);
                logger.info(jobId + " reset at " + new Date());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }


    /****
     * 暂停任务
     */
    public void pauseJob(String jobId) {
        try {
            if (scheduler.checkExists(new JobKey(jobId))){
                scheduler.pauseJob(new JobKey(jobId));
                logger.info(jobId + " pause at " + new Date());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 删除任务
     * @param jobId
     */
    public void deleteJob(String jobId) {
        try {
            if (scheduler.checkExists(new JobKey(jobId))){
                scheduler.deleteJob(new JobKey(jobId));
                logger.info(jobId + " delete at " + new Date());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 暂停重启
     * @param jobId
     */
    public void resumeJob(String jobId) {
        try {
            if (scheduler.checkExists(new JobKey(jobId))){
                scheduler.resumeJob(new JobKey(jobId));
                logger.info(jobId + " resume at " + new Date());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 获取所有的任务 的 JobKey
     * @return
     */
    public Set<JobKey> currentJobs(){
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            return scheduler.getJobKeys(matcher);
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     * 获取任务详情 JobDetail
     * @param jobKey
     * @return
     */
    public JobDetail getJobDetail(JobKey jobKey){
        try {
            return scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取触发器 Trigger
     * @param jobKey
     * @return
     */
    public Trigger getJobTrigger(JobKey jobKey){
        try {
            return scheduler.getTrigger(new TriggerKey(TRIGGER_PERFIX + jobKey.getName()));
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取触发器 任务的执行状态
     * @param jobKey
     * @return
     */
    public Trigger.TriggerState getTriggerState(JobKey jobKey){
        try {
            return scheduler.getTriggerState(new TriggerKey(TRIGGER_PERFIX + jobKey.getName()));
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

}
