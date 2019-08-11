package com.hlj.proj.service.flow.service.enums;



/**
 * @author HealerJean
 * @ClassName FlowServiceNodeEnum
 * @Date 2019-08-11  18:48.
 * @Description 流程节点枚举
 */

public enum FlowServiceNodeEnum {


    demoJobSubmit("demoJobSubmit","任务提交","ServiceNode"),
    demoAJobDeal("demoAJobDeal","任务A处理","ServiceNode"),
    demoBJobDeal("demoBJobDeal","任务B处理","ServiceNode"),
    demoCJobDeal("demoCJobDeal","任务C处理","ServiceNode"),
    demoJobsuccess("demoJobsuccess","任务完成","ServiceNode"),



    auditJobSubmit("auditJobSubmit","审核任务提交","ServiceNode"),
    auditServiceAJobDeal("auditServiceAJobDeal","审核任务ServiceA处理","ServiceNode"),
    auditServiceBJobDeal("auditServiceBJobDeal","审核任务ServiceB处理","ServiceNode"),
    auditJobsuccess("auditJobsuccess","审核任务完成","ServiceNode"),



    ;



    private String nodeCode;
    private String nodeName;
    private String nodeType;

    FlowServiceNodeEnum(String nodeCode, String nodeName, String nodeType) {
        this.nodeCode = nodeCode;
        this.nodeName = nodeName;
        this.nodeType = nodeType;
    }

    public static FlowServiceNodeEnum toEnum(String nodeCode) {
        for (FlowServiceNodeEnum item : FlowServiceNodeEnum.values()) {
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
