package com.hlj.proj.service.flow.service;

import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.service.flow.base.entity.Result;
import com.hlj.proj.service.flow.base.entity.ServiceFlowNode;
import com.hlj.proj.service.flow.service.enums.FlowServiceNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName AuthenticationServiceFlowNode
 * @Author TD
 * @Date 2019/6/12 15:18
 * @Description 实名认证提交流程节点
 */
@Component("authenticationSubmit")
@Slf4j
public class AuthenticationSubmitServiceFlowNode extends ServiceFlowNode {


    public AuthenticationSubmitServiceFlowNode() {
        super();
        init(FlowServiceNodeEnum.authenticationSubmit.getNodeCode(),
                FlowServiceNodeEnum.authenticationSubmit.getNodeName(),
                FlowServiceNodeEnum.authenticationSubmit.getNodeType());
    }

    public AuthenticationSubmitServiceFlowNode(String data) {
        super(FlowServiceNodeEnum.authenticationSubmit.getNodeCode(),
                FlowServiceNodeEnum.authenticationSubmit.getNodeName(),
                FlowServiceNodeEnum.authenticationSubmit.getNodeType(), data);
    }

    public AuthenticationSubmitServiceFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
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
