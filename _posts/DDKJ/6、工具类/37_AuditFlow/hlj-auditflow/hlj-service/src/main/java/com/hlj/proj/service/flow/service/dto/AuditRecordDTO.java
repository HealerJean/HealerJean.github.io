package com.hlj.proj.service.flow.service.dto;

import lombok.Data;


/**
 * @ClassName AuditRecordDTO
 * @Author TD
 * @Date 2019/6/19 15:43
 * @Description 审批对象
 */
@Data
public class AuditRecordDTO {
    /**
     * 临时表ID
     */
    private Long tempId;
    /**
     * 客户Id
     */
    private String customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 流程名称
     */
    private String flowName;
    /**
     * 流程编号
     */
    private String flowCode;
    /**
     * 节点编码
     */
    private String nodeCode;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 发起人ID
     */
    private Long createrUser;
    /**
     * 发起人姓名
     */
    private String createrUserName;
    /**
     * 发起时间
     */
    private java.util.Date createTime;
    /**
     * 审核参数
     */
    private Object data;
    /**
     * 流程执行步骤号
     */
    private Integer sept;
    /**
     * 流程执行步骤号
     */
    private Integer auditSept;
    /**
     * 流程实例编号
     */
    private String instantsNo;
    /**
     * 数量
     */
    private Integer count;
}
