package com.healerjean.proj.service.core.flow.node;

import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.service.core.flow.dto.NodeTransferDataDTO;
import com.healerjean.proj.service.core.flow.dto.ServiceNodeResult;
import com.healerjean.proj.service.core.flow.enums.FlowEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * @author HealerJean
 * @ClassName FlowWorkNodeService
 * @date 2019/11/11  15:58.
 * @Description 服务节点
 */
@Data
@Slf4j
public abstract class FlowWorkNodeService {

    /**
     * 流程实例编号
     */
    private String instantsNo;
    /**
     * 流程编号
     */
    private String flowCode;
    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 节点编号
     */
    private String nodeCode;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点类型枚举
     */
    private FlowEnum.FlowNodeTypeEnum flowNodeTypeEnum;
    /**
     * 流程执行步骤号
     */
    private Integer step;
    /**
     * 状态
     */
    private String status;

    public void dealBusiness(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
        log.info("工作流：【{}】--------实例编号：【{}】--------节点：【{}】--------第【{}】步--------开始执行", flowName, instantsNo, nodeName, step);
        deal(nodeTransferDataDTO, loginUserDTO);
    }

    /**
     * 执行该节点业务
     */
    protected abstract void deal(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO);

    /**
     * 节点执行失败，需要处理的业务流程
     */
    protected abstract void fail(NodeTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO);

    public boolean nextFlowNode(NodeBusisinessEnumTransferDataDTO nodeTransferDataDTO, LoginUserDTO loginUserDTO) {
        if (nodeTransferDataDTO.getNext()) {
            FlowWorkNodeProcess.successForNextWorkNode(instantsNo, loginUserDTO);
            return true;
        }
        return false;
    }


}
