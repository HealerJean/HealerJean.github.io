package com.hlj.proj.service.flow.service;


import com.hlj.proj.dto.PageDTO;
import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.AuditorFlowNode;
import com.hlj.proj.service.flow.base.entity.AuditorResult;
import com.hlj.proj.service.flow.base.entity.ProcessDefinition;
import com.hlj.proj.service.flow.service.dto.AuditRecordDTO;

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
    List<AuditRecordDTO> queryAllGroupBy(IdentityInfoDTO identityInfo);

    /**
     *  查询审批结点数据(分页)
     */
    PageDTO<AuditRecordDTO> queryForPage(AuditRecordDTO auditRecordDTO, IdentityInfoDTO identityInfo);

    /**
     *  查询指定审批结点详情数据
     */
    AuditRecordDTO queryDetail(AuditRecordDTO auditRecordDTO);

    /**
     *  对指定审批节点进行审批
     */
    void audit(AuditorResult result, IdentityInfoDTO identityInfo);

    /**
     *  对指定审批节点进行审批
     */
    AuditorFlowNode generateAuditorFlowNode(AuditorFlowNode node, IdentityInfoDTO identityInfo);
    /**
     *  创建一个流程定义
     */
    ProcessDefinition createFlowDefinition(ProcessDefinition definition, IdentityInfoDTO identityInfo);
    /**
     *  创建一个流程定义
     */
    ProcessDefinition queryFlowDefinition(String flowCode);
}
