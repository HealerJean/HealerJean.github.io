package com.healerjean.proj.dto;

import lombok.Data;

/**
 * @author HealerJean
 * @ClassName ScheduleJobDTO
 * @date 2020/5/15  16:22.
 * @Description
 */
@Data
public class ScheduleJobDTO {

    private Long scheduleJobid;
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
