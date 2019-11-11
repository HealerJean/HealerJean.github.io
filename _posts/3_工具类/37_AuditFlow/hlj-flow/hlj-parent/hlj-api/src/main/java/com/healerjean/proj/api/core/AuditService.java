package com.healerjean.proj.api.core;

import com.healerjean.proj.dto.flow.AuditDefaultConfigDTO;
import com.healerjean.proj.dto.flow.AuditTaskDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;

/**
 * @author HealerJean
 * @ClassName AuditService
 * @date 2019-11-03  15:47.
 * @Description 工作流Serivice
 */
public interface AuditService {


    /**
     * 配置默认审批人
     */
    void configDefultAuditUser(AuditDefaultConfigDTO auditDefaultConfigDTO, LoginUserDTO loginUserDTO);

    /**
     * 发起审批申请
     */
    void taskAuditApply(AuditTaskDTO auditTaskDTO, LoginUserDTO loginUserDTO);

    /**
     * 审批成功
     */
    void auditSuccess(AuditTaskDTO auditTaskDTO, LoginUserDTO loginUserDTO);

    /**
     * 审批拒绝
     */
    void auditReject(AuditTaskDTO auditTaskDTO, LoginUserDTO loginUserDTO);

}
