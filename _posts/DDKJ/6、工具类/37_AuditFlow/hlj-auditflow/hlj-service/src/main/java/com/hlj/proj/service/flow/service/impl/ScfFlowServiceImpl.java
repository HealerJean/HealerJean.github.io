package com.hlj.proj.service.flow.service.impl;

import com.hlj.proj.data.dao.mybatis.manager.flow.FlowRefAuditorEventManager;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowAuditRecordManager;
import com.hlj.proj.data.pojo.flow.FlowRefAuditorEvent;
import com.hlj.proj.data.pojo.flow.FlowRefAuditorEventQuery;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecord;
import com.hlj.proj.dto.system.RoleDTO;
import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.exception.BusinessException;
import com.hlj.proj.service.flow.base.entity.AuditorProcess;
import com.hlj.proj.service.flow.base.entity.AuditorResult;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.service.ScfFlowService;
import com.hlj.proj.service.flow.service.enums.FlowAuditType;
import com.hlj.proj.utils.EmptyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ScfFlowAuditRecordManager scfFlowAuditRecordManager ;
    @Autowired
    private FlowRefAuditorEventManager flowRefAuditorEventManager ;

    /**
     * 对指定审批节点进行审批
     * 1、判断该用户是否有审核权限
     * 2、启动审批流
     * 3、开始审批
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void audit(AuditorResult auditorResult, IdentityInfoDTO identityInfo) {
        // 1、判断该用户是否有审核权限
        ScfFlowAuditRecord  auditRecord = scfFlowAuditRecordManager.findById(auditorResult.getAuditRecordId());
        if(auditorResult == null){
            log.error("执行时找不到对应的审批记录，auditorResult：{}", auditorResult);
            throw new BusinessException("执行时找不到对应的审批记录");
        }
        if(StringUtils.equals(auditRecord.getStatus(), Result.StatusEnum.Success.getCode())){
            log.error("已经审核了，无需重复审核");
            throw new BusinessException("已经审核了，无需重复审核");
        }
        if(!auditPermission(auditorResult, identityInfo)){
            throw new BusinessException("该用户没有审批该记录权限");
        }

        // 2、启动审批流
        AuditorProcess auditorProcess = AuditorProcess.of(auditRecord.getId());

        // 3、开始审批
        auditorProcess.audit(auditorResult, identityInfo);
        auditorResult.setInstantsNo(auditRecord.getInstantsNo());
        auditorResult.setSept(auditRecord.getSept());
    }


    /**
     * 1、判断角色是否可以审核
     * 2、判断用户是否可以审核
     */
    private Boolean auditPermission(AuditorResult auditorResult, IdentityInfoDTO identityInfo) {

        FlowRefAuditorEventQuery auditorEventQuery = new FlowRefAuditorEventQuery();
        auditorEventQuery.setRefFlowAuditRecordId(auditorResult.getAuditRecordId());

        // 1、判断角色是否可以审核
        List<RoleDTO> roleDtos = identityInfo.getRoles();
        List<Long> roles = null;
        if(roleDtos != null && !roleDtos.isEmpty()){
            roles = roleDtos.stream().map(RoleDTO::getId).collect(Collectors.toList());
        }
        auditorEventQuery.setAuditType(FlowAuditType.ROLE.getType());
        auditorEventQuery.setRoleIds(roles);
        List<FlowRefAuditorEvent> flowRefAuditorEvents = flowRefAuditorEventManager.queryList(auditorEventQuery);
        if(!EmptyUtil.isEmpty(flowRefAuditorEvents)){
            return true ;
        }

        // 2、判断用户是否可以审核
        Long userId = identityInfo.getUserId();
        auditorEventQuery.setAuditType(FlowAuditType.ID.getType());
        auditorEventQuery.setAuditObject(userId);
        flowRefAuditorEvents = flowRefAuditorEventManager.queryList(auditorEventQuery);
        if(!EmptyUtil.isEmpty(flowRefAuditorEvents)){
            return true ;
        }
        return false ;
    }


}
