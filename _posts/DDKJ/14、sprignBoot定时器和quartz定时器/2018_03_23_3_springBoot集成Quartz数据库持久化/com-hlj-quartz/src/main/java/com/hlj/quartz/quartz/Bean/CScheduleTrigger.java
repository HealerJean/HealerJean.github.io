package com.hlj.quartz.quartz.Bean;



import javax.persistence.*;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/23  下午4:11.
 */


@Entity
@Table(name = "c_schedule_triggers")
public class CScheduleTrigger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cron;  //时间表达式

    private String status; //使用状态 0：禁用   1：启用

    private String jobName; //任务名称

    private String jobGroup; //任务分组

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
}