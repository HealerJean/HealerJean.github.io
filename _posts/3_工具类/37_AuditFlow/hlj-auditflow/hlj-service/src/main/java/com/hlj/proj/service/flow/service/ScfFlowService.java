package com.hlj.proj.service.flow.service;


import com.hlj.proj.dto.PageDTO;
import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.service.dto.AuditJobCollectDTO;
import com.hlj.proj.service.flow.service.dto.AuditRecordDTO;
import com.hlj.proj.service.flow.service.dto.AuditorResultDTO;

import java.util.List;

/**
 * @ClassName ScfFlowService
 * @Author TD
 * @Date 2019/6/19 15:34
 * @Description 供应链流程审批业务类
 */
public interface ScfFlowService {

    /**
     *  查询当前所有待审批节点
     */
    List<AuditJobCollectDTO> jobCollect(IdentityInfoDTO authUser);


    /**
     *  对指定审批节点进行审批
     */
    void audit(AuditorResultDTO result, IdentityInfoDTO identityInfo);


    /**
     * 查询任务列表
     */
    PageDTO<AuditRecordDTO> readyAudits(AuditJobCollectDTO auditRecordDTO, IdentityInfoDTO authUser);

}
