package com.hlj.proj.service.flow.base.entity;


import com.hlj.proj.dto.user.IdentityInfoDTO;

/**
 * @ClassName FlowNode
 * @Author TD
 * @Date 2019/6/12 9:41
 * @Description 流程节点
 */

public abstract class FlowNode {

    protected FlowNode(){}

    protected FlowNode(String nodeCode,String nodeName,String nodeType ,String nodeServiceType ,String data){
        this.nodeCode = nodeCode;
        this.nodeServiceType = nodeServiceType;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.data = data;
    }

    /**
     * 节点编号
     */
    private String nodeCode;

    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 节点业务类型
     */
    private String nodeServiceType;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 数据
     */
    private String data;

    /**
     * 执行该节点业务
     */
    protected abstract Result  deal(String instantsNo, String data, IdentityInfoDTO identityInfo);

    /**
     * 执行该节点业务
     */
    public Result dealBusiness(String instantsNo, String dataJson, IdentityInfoDTO identityInfo){
        this.data = dataJson;
        //查询对应节点数据，修改
        Result deal = deal(instantsNo, dataJson, identityInfo);
        return deal;
    }

    /**
     * 节点执行失败，需要处理的业务流程
     */
    protected abstract void  fail(String instantsNo, String data, IdentityInfoDTO identityInfo);



    protected  void init(String nodeCode,String nodeName,String nodeType ){
        this.nodeCode = nodeCode;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNodeServiceType() {
        return nodeServiceType;
    }

    public void setNodeServiceType(String nodeServiceType) {
        this.nodeServiceType = nodeServiceType;
    }
}
