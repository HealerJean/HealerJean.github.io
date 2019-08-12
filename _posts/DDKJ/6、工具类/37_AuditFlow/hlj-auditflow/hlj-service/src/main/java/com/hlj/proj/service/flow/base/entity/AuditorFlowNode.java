package com.hlj.proj.service.flow.base.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hlj.proj.data.dao.mybatis.manager.flow.ScfFlowAuditRecordManager;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecord;
import com.hlj.proj.data.pojo.flow.ScfFlowAuditRecordQuery;
import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.spring.SpringContextHolder;
import com.hlj.proj.utils.EmptyUtil;
import com.hlj.proj.utils.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName AuditorFlowNode
 * @Author TD
 * @Date 2019/6/12 9:42
 * @Description 审核节点
 */
@Data
@Slf4j
public class AuditorFlowNode extends FlowNode {

    /**ki
     * 审批流程控制器
     */
    private AuditorProcess auditorProcess;

    /**
     * 初始化审批节点
     */
    public static AuditorFlowNode of(String nodeCode, String nodeDetail, String instantsNo, int index) {
        AuditorFlowNode auditorFlowNode = new AuditorFlowNode();
        //初始化审批流程
        ScfFlowAuditRecordManager scfFlowAuditRecordManager = SpringContextHolder.getBean(ScfFlowAuditRecordManager.class);
        //查询该节点在该步奏中所有的记录（有可能有多个审批步奏）
        List<ScfFlowAuditRecord> auditRecords = null;
        if (StringUtils.isNotBlank(instantsNo)) {
            ScfFlowAuditRecordQuery query = new ScfFlowAuditRecordQuery();
            query.setInstantsNo(instantsNo);
            query.setNodeCode(nodeCode);
            query.setSept(index + 1);
            auditRecords = scfFlowAuditRecordManager.queryList(query);
        }

        AuditorProcess auditorProcess = new AuditorProcess();
        ArrayList<Auditor> auditors = JsonUtils.toObject(nodeDetail, new TypeReference<ArrayList<Auditor>>() { });
        auditorProcess.setAuditors(auditors);

        if (!EmptyUtil.isEmpty(auditRecords)) {
            int auditorMax = 1;
            for (int i = 0; i < auditRecords.size(); i++) {
                ScfFlowAuditRecord scfFlowAuditRecord = auditRecords.get(i);
                Integer auditSept = scfFlowAuditRecord.getAuditSept();
                Auditor auditor = auditors.get(auditSept - 1);
                auditor.setStatus(scfFlowAuditRecord.getStatus());
                if (auditorMax < auditSept) {
                    auditorMax = auditSept;
                }
            }
            //设置当前审批到了第几步奏
            auditorProcess.setAuditSept(new AtomicInteger(auditorMax));
        }
        auditorFlowNode.setAuditorProcess(auditorProcess);
        return auditorFlowNode;
    }


    /**
     * 返回流程暂停
     * 1、最开始创建的审批的饿时候 auditorProcess.getAuditSept()== null
     * @param data
     * @return
     */
    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        if (auditorProcess.getAuditSept() == null || auditorProcess.getAuditSept().intValue() == 1) {
            List<ScfFlowAuditRecord> scfFlowAuditRecords = null;
            if(auditorProcess.getAuditSept() != null) {
                ScfFlowAuditRecordManager scfFlowAuditRecordManager = SpringContextHolder.getBean(ScfFlowAuditRecordManager.class);
                ScfFlowAuditRecordQuery scfFlowAuditRecordQuery = new ScfFlowAuditRecordQuery();
                scfFlowAuditRecordQuery.setInstantsNo(instantsNo);
                //查询审批记录
                scfFlowAuditRecords = scfFlowAuditRecordManager.queryList(scfFlowAuditRecordQuery);
            }
            if (EmptyUtil.isEmpty(scfFlowAuditRecords)) {
                auditorProcess.initAudit(this, instantsNo, data,identityInfo);
            }
        }
        if (auditorProcess.isReject()) {
            log.info("执行审核流程 ：{}，失败 ", getNodeName());
            return Result.fail(data);
        } else if (auditorProcess.isSuccess()) {
            log.info("执行审核流程 ：{}，成功 ", getNodeName());
            return Result.success(data);
        }
        log.info("执行审核流程 ：{}，待审批 ", getNodeName());
        return Result.suspend(data);
    }


    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {

    }
}
