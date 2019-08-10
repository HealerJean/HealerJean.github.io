package com.hlj.proj.service.flow.base.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowAuditRecordManager;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowAuditRecordTempManager;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowNodeManager;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowRecordManager;
import com.hlj.proj.data.pojo.flow.*;
import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.exception.BusinessException;
import com.hlj.proj.service.flow.service.enums.FlowAuditType;
import com.hlj.proj.service.spring.SpringContextHolder;
import com.hlj.proj.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName AuditorProcess
 * @Author TD
 * @Date 2019/6/12 15:15
 * @Description 审批流程
 */
@Data
@Slf4j
public class AuditorProcess {


    /**
     * 审批链条
     */
    private String instantsNo;
    /**
     * 审批链条
     */
    private List<Auditor> auditors;
    /**
     * 流程执行步骤号
     */
    private AtomicInteger auditSept;

    public static AuditorProcess of(String instantsNo, Integer sept, Integer auditSept) {
        ScfFlowAuditRecordManager scfFlowAuditRecordManager = SpringContextHolder.getBean(ScfFlowAuditRecordManager.class);
        ScfFlowNodeManager scfFlowNodeManager = SpringContextHolder.getBean(ScfFlowNodeManager.class);
        ScfFlowAuditRecordQuery scfFlowAuditRecordQuery = new ScfFlowAuditRecordQuery();
        scfFlowAuditRecordQuery.setInstantsNo(instantsNo);
        scfFlowAuditRecordQuery.setSept(sept);
        scfFlowAuditRecordQuery.setAuditSept(auditSept);
        //查询审批记录
        ScfFlowAuditRecord scfFlowAuditRecord = scfFlowAuditRecordManager.findByQueryContion(scfFlowAuditRecordQuery);
        AuditorProcess auditorProcess = new AuditorProcess();
        auditorProcess.setAuditSept(new AtomicInteger(scfFlowAuditRecord.getAuditSept()));
        String nodeCode = scfFlowAuditRecord.getNodeCode();
        //查询流程节点
        ScfFlowNodeQuery scfFlowNodeQuery = new ScfFlowNodeQuery();
        scfFlowNodeQuery.setNodeCode(nodeCode);
        ScfFlowNode scfFlowNode = scfFlowNodeManager.findByQueryContion(scfFlowNodeQuery);
        String nodeDetail = scfFlowNode.getNodeDetail();
        if (StringUtils.isBlank(nodeDetail)) {
            auditorProcess.setAuditors(new ArrayList<>(0));
        } else {
            ArrayList<Auditor> auditors =  JsonUtils.toObject(nodeDetail,new TypeReference<ArrayList<Auditor>>() { });
            auditorProcess.setAuditors(auditors);
        }
        auditorProcess.instantsNo = instantsNo;
        return auditorProcess;
    }


    /**
     * 审核开始
     */
    public void initAudit(AuditorFlowNode auditorFlowNode, String instantsNo, String data) {
        generateAuditRecord(instantsNo, auditorFlowNode.getNodeCode(), 1, data);
    }

    public Boolean isSuccess() {
        int i = auditSept.intValue();
        if(i > auditors.size() ){
            return true;
        }
        Auditor auditor = auditors.get(i - 1);
        return auditSept.intValue() == auditors.size()
                && Result.StatusEnum.Success.getCode().equals(auditor.getStatus());
    }

    public Boolean isReject() {
        int i = auditSept.intValue();
        Auditor auditor = auditors.get(i - 1);
        return Result.StatusEnum.Fail.getCode().equals(auditor.getStatus());
    }


    /**
     * 同意(true)/(false)拒绝当前审批
     */
    public void audit(AuditorResult auditorResult, IdentityInfoDTO identityInfo) {
        DataSourceTransactionManager transactionManager = SpringContextHolder.getBean(DataSourceTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int i = auditSept.intValue();
            Auditor auditor = auditors.get(i - 1);
            ScfFlowAuditRecordManager scfFlowAuditRecordManager = SpringContextHolder.getBean(ScfFlowAuditRecordManager.class);
            ScfFlowAuditRecordTempManager scfFlowAuditRecordTempManager = SpringContextHolder.getBean(ScfFlowAuditRecordTempManager.class);
            ScfFlowAuditRecordQuery scfFlowAuditRecordQuery = new ScfFlowAuditRecordQuery();
            scfFlowAuditRecordQuery.setInstantsNo(auditorResult.getInstantsNo());
            scfFlowAuditRecordQuery.setSept(auditorResult.getSept());
            scfFlowAuditRecordQuery.setAuditSept(auditorResult.getAuditSept());
            scfFlowAuditRecordQuery.setStatus(Result.StatusEnum.Suspend.getCode());
            ScfFlowAuditRecord scfFlowAuditRecord = scfFlowAuditRecordManager.findByQueryContion(scfFlowAuditRecordQuery);
            if (scfFlowAuditRecord == null) {
                log.error("执行时找不到对应的审批记录，auditorResult：{}", auditorResult);
                throw new BusinessException("执行时找不到对应的审批记录");
            }
            //先判断是否成功
            Boolean auditResult = auditorResult.getAuditResult();
            if (auditResult != null && auditResult) {
                //成功
                auditor.setStatus(Result.StatusEnum.Success.getCode());
            } else {
                //失败
                auditor.setStatus(Result.StatusEnum.Fail.getCode());
            }
            //保存 审核记录 数据
            scfFlowAuditRecord.setStatus(auditor.getStatus());
            scfFlowAuditRecord.setOptUser(identityInfo.getUserId());
            scfFlowAuditRecord.setOptUserName(identityInfo.getRealName());
            scfFlowAuditRecord.setOptTime(new Date());
            scfFlowAuditRecord.setAuditMessage(auditorResult.getAuditMessage());
            scfFlowAuditRecord.setRefFileIds(JsonUtils.toJsonString(auditorResult.getFileIds()));
            scfFlowAuditRecordManager.updateSelective(scfFlowAuditRecord);

            //保存 审核临时记录 ，删除多于数据
            ScfFlowAuditRecordTempQuery tempQuery = new ScfFlowAuditRecordTempQuery();
            tempQuery.setInstantsNo(auditorResult.getInstantsNo());
            tempQuery.setSept(auditorResult.getSept());
            tempQuery.setAuditSept(auditorResult.getAuditSept());
            tempQuery.setStatus(Result.StatusEnum.Suspend.getCode());
            tempQuery.setAuditType(auditorResult.getAuditType());
            tempQuery.setAuditObject(auditorResult.getAuditObject());
            ScfFlowAuditRecordTemp scfFlowAuditRecordTemp = scfFlowAuditRecordTempManager.findByQueryContion(tempQuery);
            tempQuery.setAuditType(null);
            tempQuery.setAuditObject(null);
            scfFlowAuditRecordTempManager.delete(tempQuery);
            String auditData = scfFlowAuditRecordTemp.getAuditData();
            log.info("序列化出来的数据：{}", auditData);
            //当审批流程全部走完时，则触发流程的下一步
            auditSept.incrementAndGet();
            if (isSuccess() || isReject()) {
                Process process = ProcessDefinition.ofSuspendInstant(instantsNo);
                process.nextFlow(instantsNo, auditData, identityInfo);
            } else {
                //创建下一个审批记录
                generateAuditRecord(auditorResult.getInstantsNo(), auditorResult.getNodeCode(), auditSept.intValue(), null);
            }
            transactionManager.commit(status);
        } catch (BusinessException e) {
            log.error("审批错误",e);
            transactionManager.rollback(status);
            throw e;
        }  catch (Exception e) {
            log.error("审批错误",e);
            transactionManager.rollback(status);
            throw new BusinessException("审批错误");
        }
    }


    private void generateAuditRecord(String instantsNo, String nodeCode, Integer auditSept, String data) {
        this.auditSept = new AtomicInteger(auditSept);
        ScfFlowAuditRecordManager scfFlowAuditRecordManager = SpringContextHolder.getBean(ScfFlowAuditRecordManager.class);
        ScfFlowNodeManager scfFlowNodeManager = SpringContextHolder.getBean(ScfFlowNodeManager.class);
        ScfFlowAuditRecordTempManager scfFlowAuditRecordTempManager = SpringContextHolder.getBean(ScfFlowAuditRecordTempManager.class);
        ScfFlowRecordManager scfFlowRecordManager = SpringContextHolder.getBean(ScfFlowRecordManager.class);
        ScfFlowRecordQuery scfFlowRecordQuery = new ScfFlowRecordQuery();
        scfFlowRecordQuery.setInstantsNo(instantsNo);
        scfFlowRecordQuery.setStatus(Result.StatusEnum.Suspend.getCode());
        scfFlowRecordQuery.setNodeCode(nodeCode);
        ScfFlowRecord scfFlowRecord = scfFlowRecordManager.findByQueryContion(scfFlowRecordQuery);
        if (scfFlowRecord == null) {
            log.error("执行时找不到对应的流程记录，instantsNo：{}", instantsNo);
            throw new BusinessException("执行时找不到对应的流程记录");
        }
        ScfFlowNodeQuery scfFlowNodeQuery = new ScfFlowNodeQuery();
        scfFlowNodeQuery.setNodeCode(nodeCode);
        ScfFlowNode scfFlowNode = scfFlowNodeManager.findByQueryContion(scfFlowNodeQuery);
        //创建第一个审核记录
        ScfFlowAuditRecord scfFlowAuditRecord = new ScfFlowAuditRecord();
        scfFlowAuditRecord.setInstantsNo(scfFlowRecord.getInstantsNo());
        scfFlowAuditRecord.setSept(scfFlowRecord.getSept());
        scfFlowAuditRecord.setFlowCode(scfFlowRecord.getFlowCode());
        scfFlowAuditRecord.setFlowName(scfFlowRecord.getFlowName());
        scfFlowAuditRecord.setNodeCode(scfFlowRecord.getNodeCode());
        scfFlowAuditRecord.setNodeName(scfFlowRecord.getNodeName());
        scfFlowAuditRecord.setAuditSept(auditSept);
        scfFlowAuditRecord.setStatus(Result.StatusEnum.Suspend.getCode());
        scfFlowAuditRecordManager.insertSelective(scfFlowAuditRecord);
        Auditor auditor = auditors.get(auditSept - 1);
        if (auditor != null) {
            List<ScfFlowAuditRecordTemp> list = new ArrayList<>();
            List<Long> ids = auditor.getIds();
            List<Long> roles = auditor.getRoles();
            if (ids != null && !ids.isEmpty()) {
                for (int i = 0; i < ids.size(); i++) {
                    ScfFlowAuditRecordTemp scfFlowAuditRecordTemp = new ScfFlowAuditRecordTemp();
                    scfFlowAuditRecordTemp.setInstantsNo(scfFlowRecord.getInstantsNo());
                    scfFlowAuditRecordTemp.setSept(scfFlowRecord.getSept());
                    scfFlowAuditRecordTemp.setFlowCode(scfFlowRecord.getFlowCode());
                    scfFlowAuditRecordTemp.setFlowName(scfFlowRecord.getFlowName());
                    scfFlowAuditRecordTemp.setCreateUser(scfFlowRecord.getCreateUser());
                    scfFlowAuditRecordTemp.setCreateName(scfFlowRecord.getCreateName());
                    scfFlowAuditRecordTemp.setCreateTime(scfFlowRecord.getCreateTime());
                    scfFlowAuditRecordTemp.setNodeCode(scfFlowRecord.getNodeCode());
                    scfFlowAuditRecordTemp.setNodeServiceType(scfFlowNode.getNodeServiceType());
                    scfFlowAuditRecordTemp.setNodeName(scfFlowRecord.getNodeName());
                    scfFlowAuditRecordTemp.setAuditSept(auditSept);
                    scfFlowAuditRecordTemp.setStatus(Result.StatusEnum.Suspend.getCode());
                    scfFlowAuditRecordTemp.setAuditType(FlowAuditType.ID.getType());
                    scfFlowAuditRecordTemp.setAuditObject(ids.get(i));
                    scfFlowAuditRecordTemp.setAuditData(data);
                    list.add(scfFlowAuditRecordTemp);
                }
            }
            if (roles != null && !roles.isEmpty()) {
                for (int i = 0; i < roles.size(); i++) {
                    ScfFlowAuditRecordTemp scfFlowAuditRecordTemp = new ScfFlowAuditRecordTemp();
                    scfFlowAuditRecordTemp.setInstantsNo(scfFlowRecord.getInstantsNo());
                    scfFlowAuditRecordTemp.setSept(scfFlowRecord.getSept());
                    scfFlowAuditRecordTemp.setFlowCode(scfFlowRecord.getFlowCode());
                    scfFlowAuditRecordTemp.setFlowName(scfFlowRecord.getFlowName());
                    scfFlowAuditRecordTemp.setNodeCode(scfFlowRecord.getNodeCode());
                    scfFlowAuditRecordTemp.setNodeServiceType(scfFlowNode.getNodeServiceType());
                    scfFlowAuditRecordTemp.setNodeName(scfFlowRecord.getNodeName());
                    scfFlowAuditRecordTemp.setCreateUser(scfFlowRecord.getCreateUser());
                    scfFlowAuditRecordTemp.setCreateName(scfFlowRecord.getCreateName());
                    scfFlowAuditRecordTemp.setCreateTime(scfFlowRecord.getCreateTime());
                    scfFlowAuditRecordTemp.setAuditSept(auditSept);
                    scfFlowAuditRecordTemp.setStatus(Result.StatusEnum.Suspend.getCode());
                    scfFlowAuditRecordTemp.setAuditType(FlowAuditType.ROLE.getType());
                    scfFlowAuditRecordTemp.setAuditObject(roles.get(i));
                    scfFlowAuditRecordTemp.setAuditData(data);
                    list.add(scfFlowAuditRecordTemp);
                }
            }
            scfFlowAuditRecordTempManager.batchInsert(list);
        }
    }
}
