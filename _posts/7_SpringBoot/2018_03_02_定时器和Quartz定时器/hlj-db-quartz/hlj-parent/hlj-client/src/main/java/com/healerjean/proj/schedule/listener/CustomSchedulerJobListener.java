package com.healerjean.proj.schedule.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import java.util.Date;

/**
 * 类描述：
 * 创建人： HealerJean
 */
@Slf4j
public class CustomSchedulerJobListener implements JobListener {

    private static final String LISTENER_NAME = "healerjean.job.listener";

    @Override
    public String getName() {
        return LISTENER_NAME;
    }


    /**
     * 任务执行之前执行
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        String name = jobExecutionContext.getJobDetail().getKey().getName();
        log.info("定时器监听，任务：【{} 准备开始执行，执行时间：{}", name, new Date());
    }

    /**
     * 任务取消
     * 这个方法正常情况下不执行,但是如果当 TriggerListener中的 vetoJobExecution 方法返回true时, 那么执行这个方法.
     * 注意： 如果方法(2)执行 那么(1),(3)这个俩个方法不会执行,因为任务被终止了嘛.
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        String name = jobExecutionContext.getJobDetail().getKey().getName();
        log.info("定时器监听，任务：【{} 取消", name);
    }


    /**
     * 任务执行完成后执行,jobException如果它不为空则说明任务在执行过程中出现了异常
     */
    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        String name = jobExecutionContext.getJobDetail().getKey().getName();
        if (e != null) {
            log.error("定时器监听，任务：【{"+name+"}】 执行失败", e);
        } else {
            log.info("定时器监听，任务：【{}】执行成功，执行时间：{}", name, new Date());

        }
    }
}
