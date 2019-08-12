package com.hlj.proj.service.flow.base.entity;

import com.hlj.proj.common.ValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName AuditorResult
 * @Author TD
 * @Date 2019/6/12 21:03
 * @Description 审批
 */
@Data
public class AuditorResult {


    /**
     * 审批记录Id
     */
    private Long auditRecordId;

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
    private List<String> fileIds;

    /**
     * 流程实例流水号
     */
    private String instantsNo;
    /**
     * 流程步骤
     */
    private Integer sept;
    /**
     * 审核步骤
     */
    private Integer auditSept;
    /**
     * 节点编号
     */
    private String nodeCode;
}
