package com.healerjean.proj.service.bizlog.data.po;

import lombok.*;

import java.util.Date;

/**
 * 分析出需要记录的操作日志
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogRecord {

    /**
     * Id
     */
    private Integer id;

    /**
     * 租户，是为了多租户使用的
     */
    private String tenant;

    /**
     * bizKey
     */
    private String bizKey;

    /**
     * bizNo
     */
    private String bizNo;

    /**
     * operator
     */
    private String operator;

    /**
     * action
     */
    private String action;

    /**
     * category
     */
    private String category;

    /**
     * createTime
     */
    private Date createTime;

    /**
     * detail
     */
    private String detail;
}