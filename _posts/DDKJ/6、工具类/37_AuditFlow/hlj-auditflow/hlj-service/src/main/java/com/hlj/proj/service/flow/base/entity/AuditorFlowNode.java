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


    /**
     * 审批流程控制器
     */
    private AuditorProcess auditorProcess;

    /**
     * 初始化审批节点
     */
    public static AuditorFlowNode of(String nodeCode, String nodeName, String nodeType, String nodeDetail, String instantsNo, int index) {
        AuditorFlowNode auditorFlowNode = new AuditorFlowNode();
        auditorFlowNode.init(nodeCode, nodeName, nodeType);
        //初始化审批流程
        ScfFlowAuditRecordManager scfFlowAuditRecordManager = SpringContextHolder.getBean(ScfFlowAuditRecordManager.class);
        List<ScfFlowAuditRecord> scfFlowAuditRecords = null;
        if (StringUtils.isNotBlank(instantsNo)) {
            ScfFlowAuditRecordQuery query = new ScfFlowAuditRecordQuery();
            query.setInstantsNo(instantsNo);
            query.setNodeCode(nodeCode);
            query.setSept(index + 1);
            scfFlowAuditRecords = scfFlowAuditRecordManager.queryList(query);
        }

        AuditorProcess auditorProcess = new AuditorProcess();
        if (StringUtils.isBlank(nodeDetail)) {
            auditorProcess.setAuditors(new ArrayList<>(0));
        } else {
            ArrayList<Auditor> auditors = JsonUtils.toObject(nodeDetail, new TypeReference<ArrayList<Auditor>>() { });
            auditorProcess.setAuditors(auditors);
        }
        List<Auditor> auditors = auditorProcess.getAuditors();
        if (!EmptyUtil.isEmpty(scfFlowAuditRecords)) {
            int auditorMax = 1;
            for (int i = 0; i < scfFlowAuditRecords.size(); i++) {
                ScfFlowAuditRecord scfFlowAuditRecord = scfFlowAuditRecords.get(i);
                String nodeCodeAudit = scfFlowAuditRecord.getNodeCode();
                Integer sept = scfFlowAuditRecord.getSept();
                if(nodeCodeAudit.equals(nodeCode) && sept == index + 1) {
                    Integer auditSept = scfFlowAuditRecord.getAuditSept();
                    String status = scfFlowAuditRecord.getStatus();
                    Auditor auditor = auditors.get(auditSept - 1);
                    auditor.setStatus(status);
                    if (auditorMax < auditSept) {
                        auditorMax = auditSept;
                    }
                }
            }
            auditorProcess.setAuditSept(new AtomicInteger(auditorMax));
        }
        auditorFlowNode.setAuditorProcess(auditorProcess);
        return auditorFlowNode;
    }

    /**
     * 返回流程暂停
     * @param data
     * @return
     */
    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {

        /***
         *
         */
        if (auditorProcess.getAuditSept() == null || auditorProcess.getAuditSept().intValue() == 1) {
            ScfFlowAuditRecord scfFlowAuditRecord = null;
            if(auditorProcess.getAuditSept() != null) {
                ScfFlowAuditRecordManager scfFlowAuditRecordManager = SpringContextHolder.getBean(ScfFlowAuditRecordManager.class);
                ScfFlowAuditRecordQuery scfFlowAuditRecordQuery = new ScfFlowAuditRecordQuery();
                scfFlowAuditRecordQuery.setInstantsNo(instantsNo);
                //查询审批记录
                scfFlowAuditRecord = scfFlowAuditRecordManager.findByQueryContion(scfFlowAuditRecordQuery);
            }
            if (scfFlowAuditRecord == null) {
                auditorProcess.initAudit(this, instantsNo, data);
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
