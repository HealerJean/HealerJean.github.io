package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowServiceNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("auditServiceBJobDeal")
@Slf4j
public class AuditServiceBJobFlowNode extends ServiceFlowNode {


    public AuditServiceBJobFlowNode() {
        super();
        init(FlowServiceNodeEnum.auditServiceBJobDeal.getNodeCode(),
                FlowServiceNodeEnum.auditServiceBJobDeal.getNodeName(),
                FlowServiceNodeEnum.auditServiceBJobDeal.getNodeType());
    }

    public AuditServiceBJobFlowNode(String data) {
        super(FlowServiceNodeEnum.auditServiceBJobDeal.getNodeCode(),
                FlowServiceNodeEnum.auditServiceBJobDeal.getNodeName(),
                FlowServiceNodeEnum.auditServiceBJobDeal.getNodeType(), data);
    }

    public AuditServiceBJobFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
        super(nodeCode, nodeName, nodeType, data);
    }


    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        log.info("DemoJob执行----任务A提交执行任务-------任务A");
        return Result.success(data);
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {
    }
}
