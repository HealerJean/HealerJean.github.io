package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("demoBJobDeal")
@Slf4j
public class DemoBJobDealFlowNode extends ServiceFlowNode {

    public DemoBJobDealFlowNode() {
        super();
        init(FlowNodeEnum.demoBJobDeal.getNodeCode(),
                FlowNodeEnum.demoBJobDeal.getNodeName(),
                FlowNodeEnum.demoBJobDeal.getNodeType());
    }

    public DemoBJobDealFlowNode(String data) {
        super(FlowNodeEnum.demoBJobDeal.getNodeCode(),
                FlowNodeEnum.demoBJobDeal.getNodeName(),
                FlowNodeEnum.demoBJobDeal.getNodeType(), data);
    }

    public DemoBJobDealFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
        super(nodeCode, nodeName, nodeType, data);
    }

    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        return Result.fail(data);
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {

    }
}
