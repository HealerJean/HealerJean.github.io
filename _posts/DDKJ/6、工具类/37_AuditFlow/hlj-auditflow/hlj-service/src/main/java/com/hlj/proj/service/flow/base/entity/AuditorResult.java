package com.hlj.proj.service.flow.base.entity;

import lombok.Data;
import java.util.List;

/**
 * @ClassName AuditorResult
 * @Author TD
 * @Date 2019/6/12 21:03
 * @Description 审批
 */
@Data
public class AuditorResult {

    /** 流程实例流水号 */
    private String instantsNo;
    /** 流程步骤 */
    private Integer sept;
    /** 审核步骤 */
    private Integer auditSept;
    /** 节点编号 */
    private String nodeCode;
    /** 审核类型：角色或ID */
    private String auditType;
    /** 审核对象 */
    private Long auditObject;
    /**
     * 审批结果
     */
    private Boolean auditResult;
    /**
     * 审批意见
     */
    private String auditMessage;
    /**
     * 审批附件
     */
    private List<Long> fileIds;
    /**
     * 临时ID
     */
    private Long tempId;
}
