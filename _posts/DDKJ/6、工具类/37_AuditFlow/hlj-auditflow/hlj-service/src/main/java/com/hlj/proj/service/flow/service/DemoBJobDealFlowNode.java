package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.flowJob.DemoJobDTO;
import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowServiceNodeEnum;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


@Component("demoBJobDeal")
@Slf4j
public class DemoBJobDealFlowNode extends ServiceFlowNode {

    public DemoBJobDealFlowNode() {
        super();
        init(FlowServiceNodeEnum.demoBJobDeal.getNodeCode(),
                FlowServiceNodeEnum.demoBJobDeal.getNodeName(),
                FlowServiceNodeEnum.demoBJobDeal.getNodeType());
    }

    public DemoBJobDealFlowNode(String data) {
        super(FlowServiceNodeEnum.demoBJobDeal.getNodeCode(),
                FlowServiceNodeEnum.demoBJobDeal.getNodeName(),
                FlowServiceNodeEnum.demoBJobDeal.getNodeType(), data);
    }

    public DemoBJobDealFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
        super(nodeCode, nodeName, nodeType, data);
    }

    @Override
    public Result deal(String instantsNo, String data, IdentityInfoDTO identityInfo) {
        if (StringUtils.isNotBlank(data)) {
            DemoJobDTO demoJobDTO = JsonUtils.toObject(data, DemoJobDTO.class);
            if (demoJobDTO.getNextFlow() != null && demoJobDTO.getNextFlow()) {
                log.info(" DemoJob执行----任务B准备继续任务-------任务B");
                return Result.success(data);
            } else {
                log.info(" DemoJob执行----任务B准备暂停任务-------任务B");
                return Result.suspend(data);
            }
        } else {
            log.info(" DemoJob执行----任务B准备暂停任务-------任务B");
            return Result.suspend(data);
        }
    }

    @Override
    protected void fail(String instantsNo, String data, IdentityInfoDTO identityInfo) {

    }
}
