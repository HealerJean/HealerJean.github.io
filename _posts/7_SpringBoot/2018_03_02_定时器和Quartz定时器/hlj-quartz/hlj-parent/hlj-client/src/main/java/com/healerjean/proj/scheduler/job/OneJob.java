package com.healerjean.proj.scheduler.job;

        import lombok.extern.slf4j.Slf4j;
        import org.quartz.DisallowConcurrentExecution;
        import org.quartz.Job;
        import org.quartz.JobDetail;
        import org.quartz.JobExecutionContext;

        import java.util.Date;

@Slf4j
@DisallowConcurrentExecution //禁止并发执行多个相同定义的JobDetail
public class OneJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        log.info("quartz任务--------OneJob--------开始执行： 任务detail名称：{}，执行时间：{}", jobDetail.getKey().getName(), new Date());
    }

}
