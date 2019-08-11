package com.hlj.proj.service.flow.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowAuditRecordTempManager;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowDefinitionManager;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowNodeManager;
import com.hlj.proj.data.pojo.flow.*;
import com.hlj.proj.dto.PageDTO;
import com.hlj.proj.dto.system.RoleDTO;
import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.enums.StatusEnum;
import com.hlj.proj.exception.BusinessException;
import com.hlj.proj.service.flow.base.entity.*;
import com.hlj.proj.service.flow.service.ScfFlowService;
import com.hlj.proj.service.flow.service.dto.AuditRecordDTO;
import com.hlj.proj.service.flow.service.enums.FlowNodeTypeEnum;
import com.hlj.proj.utils.BeanUtils;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ScfFlowServiceImpl
 * @Author TD
 * @Date 2019/6/19 16:11
 * @Description
 */
@Service
@Slf4j
public class ScfFlowServiceImpl implements ScfFlowService {

    @Autowired
    private ScfFlowAuditRecordTempManager scfFlowAuditRecordTempManager;
    @Autowired
    private ScfFlowNodeManager scfFlowNodeManager;
    @Autowired
    private ScfFlowDefinitionManager scfFlowDefinitionManager;

    /**
     * 查询当前所有待审批节点
     */
    @Override
    public List<AuditRecordDTO> queryAllGroupBy(IdentityInfoDTO identityInfo) {
        List<RoleDTO> roleDtos = identityInfo.getRoles();
        List<Long> roles = null;
        if (roleDtos != null && !roleDtos.isEmpty()) {
            roles = roleDtos.stream().map(RoleDTO::getId).collect(Collectors.toList());
        }
        Long userId = identityInfo.getUserId();
        ScfFlowAuditRecordTempQuery query = new ScfFlowAuditRecordTempQuery();
        query.setUserId(userId);
        query.setRoles(roles);
        query.setStatus(Result.StatusEnum.Suspend.getCode());
        List<ScfFlowAuditRecordTemp> list = scfFlowAuditRecordTempManager.queryListGroupByFlowCode(query);
        ScfFlowNodeQuery scfFlowNodeQuery = new ScfFlowNodeQuery();
        scfFlowNodeQuery.setNodeType(FlowNodeTypeEnum.AuditNode.getType());
        List<AuditRecordDTO> collect = new ArrayList<>();
        if (list != null) {
            collect = list.stream().map(item -> BeanUtils.toAuditRecordDTO(item))
                    .collect(Collectors.toList());
        }
        List<ScfFlowNode> scfFlowNodes = scfFlowNodeManager.queryList(scfFlowNodeQuery);
        for (ScfFlowNode node : scfFlowNodes) {
            String nodeServiceType = node.getNodeServiceType();
            String nodeName = node.getNodeName();
            boolean b = collect.stream().map(item -> item.getNodeServiceType()).noneMatch(item -> item.equals(nodeServiceType));
            if (b) {
                AuditRecordDTO auditRecordDTO = new AuditRecordDTO();
                auditRecordDTO.setNodeServiceType(nodeServiceType);
                auditRecordDTO.setNodeName(nodeName);
                auditRecordDTO.setCount(0);
                collect.add(auditRecordDTO);
            }
        }
        return collect;
    }

    /**
     * 查询审批结点数据(分页)
     */
    @Override
    public PageDTO<AuditRecordDTO> queryForPage(AuditRecordDTO auditRecordDTO, IdentityInfoDTO identityInfo) {
        List<RoleDTO> roleDtos = identityInfo.getRoles();
        List<Long> roles = null;
        if (roleDtos != null && !roleDtos.isEmpty()) {
            roles = roleDtos.stream().map(RoleDTO::getId).collect(Collectors.toList());
        }
        Long userId = identityInfo.getUserId();
        ScfFlowAuditRecordTempQuery query = new ScfFlowAuditRecordTempQuery();
        query.setUserId(userId);
        query.setRoles(roles);
        query.setFlowCode(auditRecordDTO.getFlowCode());
        query.setNodeServiceType(auditRecordDTO.getNodeServiceType());
        query.setStatus(Result.StatusEnum.Suspend.getCode());
        ScfFlowAuditRecordTempPage page = scfFlowAuditRecordTempManager.queryListPageGroupByByFlowCode(query);
        List<ScfFlowAuditRecordTemp> datas = page.getValues();
        List<AuditRecordDTO> collects = null;
        if (datas != null && !datas.isEmpty()) {
            collects = datas.stream().map(item -> BeanUtils.toAuditRecordDTO(item)).collect(Collectors.toList());
        }
        return BeanUtils.toPageDTO(page, collects);
    }

    /**
     * 查询指定审批结点详情数据
     */
    @Override
    public AuditRecordDTO queryDetail(AuditRecordDTO auditRecordDTO) {
        Long tempId = auditRecordDTO.getTempId();
        ScfFlowAuditRecordTemp scfFlowAuditRecordTemp = scfFlowAuditRecordTempManager.findById(tempId);
        return BeanUtils.toAuditRecordDTO(scfFlowAuditRecordTemp);
    }

    /**
     * 对指定审批节点进行审批
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(AuditorResult auditorResult, IdentityInfoDTO identityInfo) {
        Long tempId = auditorResult.getTempId();
        ScfFlowAuditRecordTemp scfFlowAuditRecordTemp = scfFlowAuditRecordTempManager.findById(tempId);
        if (scfFlowAuditRecordTemp == null) {
            throw new BusinessException("审批对应临时记录不存在");
        }
        String instantsNo = scfFlowAuditRecordTemp.getInstantsNo();
        Integer sept = scfFlowAuditRecordTemp.getSept();
        Integer auditSept = scfFlowAuditRecordTemp.getAuditSept();
        AuditorProcess auditorProcess = AuditorProcess.of(instantsNo, sept, auditSept);
        auditorResult.setInstantsNo(instantsNo);
        auditorResult.setSept(sept);
        auditorResult.setAuditSept(auditSept);
        auditorResult.setAuditType(scfFlowAuditRecordTemp.getAuditType());
        auditorResult.setAuditObject(scfFlowAuditRecordTemp.getAuditObject());
        auditorProcess.audit(auditorResult, identityInfo);
    }

    @Override
    public AuditorFlowNode generateAuditorFlowNode(AuditorFlowNode node, IdentityInfoDTO identityInfo) {
        List<Auditor> auditors = node.getAuditorProcess().getAuditors();
        if (auditors == null || auditors.isEmpty()) {
            log.error("创建审核节点审批人列表不能为空");
            throw new BusinessException("创建审核节点审批人列表不能为空");
        }
        String jsonString = JsonUtils.toJsonString(auditors);
        ScfFlowNodeQuery query = new ScfFlowNodeQuery();
        query.setNodeName(node.getNodeName());
        query.setNodeDetail(jsonString);
        query.setStatus(StatusEnum.生效.code);
        ScfFlowNode scfFlowNode = scfFlowNodeManager.findByQueryContion(query);
        if (scfFlowNode == null) {
            scfFlowNode = new ScfFlowNode();
            scfFlowNode.setNodeCode(node.getNodeCode());
            scfFlowNode.setNodeName(node.getNodeName());
            scfFlowNode.setNodeType(FlowNodeTypeEnum.AuditNode.getType());
            scfFlowNode.setNodeServiceType(node.getNodeServiceType());
            scfFlowNode.setNodeDetail(jsonString);
            scfFlowNode.setStatus(StatusEnum.生效.code);
            scfFlowNodeManager.insertSelective(scfFlowNode);
        }
        node.setNodeCode(scfFlowNode.getNodeCode());
        node.setNodeName(scfFlowNode.getNodeName());
        return node;
    }

    @Override
    public ProcessDefinition createFlowDefinition(ProcessDefinition definition, IdentityInfoDTO identityInfo) {
        List<String> collect = definition.getNodeCodes();
        if (collect == null || collect.isEmpty()) {
            log.error("创建流程节点列表不能为空");
            throw new BusinessException("创建流程节点列表不能为空");
        }
        String jsonString = JsonUtils.toJsonString(collect);
        ScfFlowDefinitionQuery query = new ScfFlowDefinitionQuery();
        query.setFlowDefinition(jsonString);
        query.setStatus(StatusEnum.生效.code);
        ScfFlowDefinition scfFlowDefinition = scfFlowDefinitionManager.findByQueryContion(query);
        if (scfFlowDefinition == null) {
            scfFlowDefinition = new ScfFlowDefinition();
            scfFlowDefinition.setFlowCode(definition.getFlowCode());
            scfFlowDefinition.setFlowName(definition.getFlowName());
            scfFlowDefinition.setFlowDefinition(jsonString);
            scfFlowDefinition.setStatus(StatusEnum.生效.code);
            scfFlowDefinitionManager.insertSelective(scfFlowDefinition);
        }
        definition.setFlowCode(scfFlowDefinition.getFlowCode());
        return definition;
    }

    @Override
    public ProcessDefinition queryFlowDefinition(String flowCode) {
        if (StringUtils.isBlank(flowCode)) {
            return null;
        }
        ScfFlowDefinitionQuery query = new ScfFlowDefinitionQuery();
        query.setFlowCode(flowCode);
        query.setStatus(StatusEnum.生效.code);
        ScfFlowDefinition scfFlowDefinition = scfFlowDefinitionManager.findByQueryContion(query);
        ProcessDefinition definition = new ProcessDefinition();
        definition.setFlowCode(scfFlowDefinition.getFlowCode());
        definition.setFlowName(scfFlowDefinition.getFlowName());
        String flowDefinition = scfFlowDefinition.getFlowDefinition();
        List<String> flowNodeStrings = JsonUtils.toObject(flowDefinition, new TypeReference<List<String>>() {
        });
        definition.setNodeCodes(flowNodeStrings);
        return definition;
    }
}
