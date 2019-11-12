package com.hlj.web.quartz.vo;

import java.io.Serializable;
import java.util.Date;

/**
 *            JobDetailBean detailBean = new JobDetailBean();
 *             detailBean.setJobId(jobKey.getName());
 *             detailBean.setJobDesc(jobDetail.getDescription());
 *             detailBean.setCron(((CronTrigger)trigger).getCronExpression());
 *             detailBean.setJobClass(jobDetail.getJobClass().toString());
 *             detailBean.setPreviousFireTime(trigger.getPreviousFireTime());
 *             detailBean.setNextFireTime(trigger.getNextFireTime());
 *
 *             Trigger.TriggerState triggerState = scheduler.getTriggerState(jobKey);
 *             detailBean.setJobStatus(triggerState.name());
 *
 *             jobDetailList.add(detailBean);
 *
 */
public class JobDetailBean implements Serializable {

    private String jobId;
    private String jobDesc;

    private String cron;
    private String jobClass;
    private String jobStatus;

    private Date previousFireTime;
    private Date nextFireTime;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
}
