package com.hlj.proj.service.flow.service.enums;

/**
 * @ClassName ServiceEnum
 * @Author TD
 * @Date 2019/6/13 10:08
 * @Description 流程节点枚举
 */
public enum FlowNodeEnum {


    demoJobSubmit("demoJobSubmit","任务提交","ServiceNode"),
    demoAJobDeal("demoAJobDeal","任务A处理","ServiceNode"),
    demoBJobDeal("demoBJobDeal","任务B处理","ServiceNode"),
    demoCJobDeal("demoCJobDeal","任务C处理","ServiceNode"),
    demoJobsuccess("demoJobsuccess","任务完成","ServiceNode"),


    ;



    private String nodeCode;
    private String nodeName;
    private String nodeType;

    FlowNodeEnum(String nodeCode, String nodeName, String nodeType) {
        this.nodeCode = nodeCode;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
    }

    public static FlowNodeEnum toEnum(String nodeCode) {
        for (FlowNodeEnum item : FlowNodeEnum.values()) {
            if (item.getNodeCode().equals(nodeCode)) {
                return item;
            }
        }
        return null;
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
}
