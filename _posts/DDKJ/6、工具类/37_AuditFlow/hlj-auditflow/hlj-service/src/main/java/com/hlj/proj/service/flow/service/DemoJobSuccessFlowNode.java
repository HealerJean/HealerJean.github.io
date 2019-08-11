package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowServiceNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("demoJobsuccess")
@Slf4j
public class DemoJobSuccessFlowNode extends ServiceFlowNode {


    public DemoJobSuccessFlowNode() {
        super();
        init(FlowServiceNodeEnum.demoJobsuccess.getNodeCode(),
                FlowServiceNodeEnum.demoJobsuccess.getNodeName(),
                FlowServiceNodeEnum.demoJobsuccess.getNodeType());
    }

    public DemoJobSuccessFlowNode(String data) {
        super(FlowServiceNodeEnum.demoJobsuccess.getNodeCode(),
                FlowServiceNodeEnum.demoJobsuccess.getNodeName(),
                FlowServiceNodeEnum.demoJobsuccess.getNodeType(), data);
    }

    public DemoJobSuccessFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
        super(nodeCode, nodeName, nodeType, data);
    }


    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        log.info("DemoJob执行----任务DemoSuccess-------DemoSuccess全部完成");
        return Result.success(data);
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {
    }
}
