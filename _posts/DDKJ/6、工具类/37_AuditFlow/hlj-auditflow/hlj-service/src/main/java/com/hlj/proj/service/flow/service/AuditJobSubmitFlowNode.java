package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowServiceNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("auditJobSubmit")
@Slf4j
public class AuditJobSubmitFlowNode extends ServiceFlowNode {


    public AuditJobSubmitFlowNode() {
        super();
        init(FlowServiceNodeEnum.auditJobSubmit.getNodeCode(),
                FlowServiceNodeEnum.auditJobSubmit.getNodeName(),
                FlowServiceNodeEnum.auditJobSubmit.getNodeType());
    }

    public AuditJobSubmitFlowNode(String data) {
        super(FlowServiceNodeEnum.auditJobSubmit.getNodeCode(),
                FlowServiceNodeEnum.auditJobSubmit.getNodeName(),
                FlowServiceNodeEnum.auditJobSubmit.getNodeType(), data);
    }

    public AuditJobSubmitFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
        super(nodeCode, nodeName, nodeType, data);
    }


    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        log.info("审核任务执行----auditJobSubmit-------审核任务提交");
        return Result.success(data);
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        System.out.println("-----------审核失败-------------------");
    }


}
