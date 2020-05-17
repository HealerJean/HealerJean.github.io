package com.healerjean.proj.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * @author HealerJean
 * @Description
 */
@Data
public class ScheduleJob implements Serializable {


    private Long id;
    /**  时间表达式 */
    private String cron;
    /**  任务名称 */
    private String jobName;
    /**  任务详情 */
    private String jobDesc;
    /**  任务类全名，包含包 */
    private String jobClass;
    /**  99 废弃 10 有效 */
    private String status;



}
