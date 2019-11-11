package com.healerjean.proj.service.core.flow.node.auditNode;

import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.service.core.flow.dto.NodeTransferDataDTO;
import com.healerjean.proj.service.core.flow.dto.ServiceNodeResult;
import com.healerjean.proj.service.core.flow.node.FlowWorkNodeService;

/**
 * @author HealerJean
 * @ClassName ContractSignApprovalAuditNode
 * @date 2019/11/11  16:13.
 * @Description
 */
public class ContractSignApprovalAuditNode extends FlowWorkNodeService {


    /**
     * 执行该节点业务
     */
    @Override
    protected void deal(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
    }

    /**
     * 节点执行失败，需要处理的业务流程
     */
    @Override
    protected void fail(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
    }
}
