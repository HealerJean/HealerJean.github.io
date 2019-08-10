package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component("demoCJobDeal")
@Slf4j
public class DemoCJobDealFlowNode extends ServiceFlowNode {


    public DemoCJobDealFlowNode() {
        super();
        init(FlowNodeEnum.demoCJobDeal.getNodeCode(),
                FlowNodeEnum.demoCJobDeal.getNodeName(),
                FlowNodeEnum.demoCJobDeal.getNodeType());
    }

    public DemoCJobDealFlowNode(String data) {
        super(FlowNodeEnum.demoCJobDeal.getNodeCode(),
                FlowNodeEnum.demoCJobDeal.getNodeName(),
                FlowNodeEnum.demoCJobDeal.getNodeType(), data);
    }

    public DemoCJobDealFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
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
