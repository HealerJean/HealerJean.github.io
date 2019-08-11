package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowServiceNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("demoCJobDeal")
@Slf4j
public class DemoCJobDealFlowNode extends ServiceFlowNode {


    public DemoCJobDealFlowNode() {
        super();
        init(FlowServiceNodeEnum.demoCJobDeal.getNodeCode(),
                FlowServiceNodeEnum.demoCJobDeal.getNodeName(),
                FlowServiceNodeEnum.demoCJobDeal.getNodeType());
    }

    public DemoCJobDealFlowNode(String data) {
        super(FlowServiceNodeEnum.demoCJobDeal.getNodeCode(),
                FlowServiceNodeEnum.demoCJobDeal.getNodeName(),
                FlowServiceNodeEnum.demoCJobDeal.getNodeType(), data);
    }

    public DemoCJobDealFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
        super(nodeCode, nodeName, nodeType, data);
    }


    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        log.info("DemoJob执行----任务C提交执行任务-------任务C");
        return Result.success(data);
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {
    }
}
