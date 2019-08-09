package com.hlj.proj.service.flow.base.entity;

/**
 * @ClassName ServiceFlowNode
 * @Author TD
 * @Date 2019/6/12 9:44
 * @Description 业务节点
 */
public abstract class ServiceFlowNode extends FlowNode {

    protected ServiceFlowNode() {
    }

    protected ServiceFlowNode(String nodeCode, String nodeName, String nodeType, String data) {
        super(nodeCode, nodeName, nodeType, null, data);
    }

}
