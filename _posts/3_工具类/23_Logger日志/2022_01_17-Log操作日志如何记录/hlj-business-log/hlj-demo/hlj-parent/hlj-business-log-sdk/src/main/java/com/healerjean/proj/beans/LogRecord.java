package com.healerjean.proj.beans;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogRecord {
    private Integer id;

    /**
     * 租户，是为了多租户使用的
     */
    private String tenant;

    private String bizKey;

    private String bizNo;

    private String operator;

    private String action;

    private String category;

    private Date createTime;

    private String detail;
}