package com.healerjean.proj.service.core.flow.node.serviceNode;

import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.service.core.flow.dto.NodeTransferDataDTO;
import com.healerjean.proj.service.core.flow.dto.ServiceNodeResult;
import com.healerjean.proj.service.core.flow.node.FlowWorkNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName LoanSubmitServiceNode
 * @date 2019/11/11  15:57.
 * @Description
 */
@Service("loanSubmit")
@Slf4j
public class LoanSubmitServiceNode extends FlowWorkNodeService {


    /**
     * 执行该节点业务
     */
    @Override
    protected void deal(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
        if (nextFlowNode(nodeTransferDataDTO, loginUserDTO)) {
            return;
        }

        log.info("工作流：【{}】--------实例编号：【{}】--------节点：【{}】--------第【{}】步--------暂停", getFlowName(), getInstantsNo(), getNodeName(), getStep());
    }

    /**
     * 节点执行失败，需要处理的业务流程
     */
    @Override
    protected void fail(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
    }
}
