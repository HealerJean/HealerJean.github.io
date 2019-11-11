package com.healerjean.proj.service.core.flow.node.serviceNode;

import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.service.core.flow.dto.NodeTransferDataDTO;
import com.healerjean.proj.service.core.flow.dto.ServiceNodeResult;
import com.healerjean.proj.service.core.flow.node.FlowWorkNodeProcess;
import com.healerjean.proj.service.core.flow.node.FlowWorkNodeService;

/**
 * @author HealerJean
 * @ClassName LoanFundServiceNode
 * @date 2019/11/11  16:14.
 * @Description
 */
public class LoanFundServiceNode extends FlowWorkNodeService {


    /**
     * 执行该节点业务
     * 1、判断是否执行下个节点
     */
    @Override
    protected void deal(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
        nextFlowNode(nodeTransferDataDTO, loginUserDTO);


    }

    /**
     * 节点执行失败，需要处理的业务流程
     */
    @Override
    protected void fail(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
    }
}
