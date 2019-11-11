package com.healerjean.proj.service.core.flow.node.auditNode;

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
@Service("riskLimitApproval")
@Slf4j
public class RiskLimitApprovalAuditNode extends FlowWorkNodeService {

    /**
     * 执行该节点业务
     */
    @Override
    protected ServiceNodeResult deal(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
        return null;
    }

    /**
     * 节点执行失败，需要处理的业务流程
     */
    @Override
    protected ServiceNodeResult fail(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
        return null;
    }
}
