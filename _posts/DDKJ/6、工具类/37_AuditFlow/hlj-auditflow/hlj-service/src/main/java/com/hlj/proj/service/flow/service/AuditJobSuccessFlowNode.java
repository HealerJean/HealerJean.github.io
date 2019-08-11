package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowServiceNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("auditJobsuccess")
@Slf4j
public class AuditJobSuccessFlowNode extends ServiceFlowNode {


    public AuditJobSuccessFlowNode() {
        super();
        init(FlowServiceNodeEnum.auditJobsuccess.getNodeCode(),
                FlowServiceNodeEnum.auditJobsuccess.getNodeName(),
                FlowServiceNodeEnum.auditJobsuccess.getNodeType());
    }

    public AuditJobSuccessFlowNode(String data) {
        super(FlowServiceNodeEnum.auditJobsuccess.getNodeCode(),
                FlowServiceNodeEnum.auditJobsuccess.getNodeName(),
                FlowServiceNodeEnum.auditJobsuccess.getNodeType(), data);
    }

    public AuditJobSuccessFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
        super(nodeCode, nodeName, nodeType, data);
    }


    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        log.info("审核任务执行----auditJobsuccess-------审核任务完成");
        return Result.success(data);
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {
    }
}
