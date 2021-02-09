package com.hlj.proj.service.flow.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName AuditorResultDTO
 * @Author TD
 * @Date 2019/6/12 21:03
 * @Description 审批
 */
@Data
public class AuditorResultDTO {
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
     * 是否可以重复发起
     */
    private Boolean again;
}
