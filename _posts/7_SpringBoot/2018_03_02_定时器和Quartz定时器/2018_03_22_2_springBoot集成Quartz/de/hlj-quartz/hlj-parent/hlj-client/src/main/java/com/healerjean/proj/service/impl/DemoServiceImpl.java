package com.healerjean.proj.service.impl;

import com.healerjean.proj.scheduler.OneJob;
import com.healerjean.proj.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class DemoServiceImpl implements DemoService {

    /**
     * 第一个quartz Job任务
     */
    @Override
    public void oneJob() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            log.info("调度器启动：scheduler.start");

            //具体任务.
            String jobDetailName = "oneJobName";
            String jobDetailGroup = "oneJobGroup";
            JobDetail jobDetail = JobBuilder.newJob(OneJob.class).withIdentity(jobDetailName, jobDetailGroup).build();
            log.info("任务详情：jobDetail：任务类：OneJob.class，任务名：{}，任务分组：{}", jobDetailName, jobDetailGroup);

            //触发时间点. (每5秒执行1次.)
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
            log.info("任务触发时间点：每5秒执行1次");

            String triggerName = "triggerName";
            String triggerGroup = "triggerGroup";
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup).startNow().withSchedule(simpleScheduleBuilder).build();
            log.info("任务触发器：触发器名称：{}，触发器分组：{}", triggerName, triggerName);

            // 交由Scheduler安排触发
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("任务详情 和 触发器 交由Scheduler安排触发");

            //睡眠20秒.等待任务完成
            TimeUnit.SECONDS.sleep(20);

            //关闭定时任务调度器.
            log.info("关闭定时器调度器");
            scheduler.shutdown();
            System.out.println("scheduler.shutdown");
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
