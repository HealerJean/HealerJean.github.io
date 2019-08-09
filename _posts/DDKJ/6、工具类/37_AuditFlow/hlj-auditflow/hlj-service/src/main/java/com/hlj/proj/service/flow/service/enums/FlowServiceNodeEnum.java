package com.hlj.proj.service.flow.service.enums;

/**
 * @ClassName ServiceEnum
 * @Author TD
 * @Date 2019/6/13 10:08
 * @Description 流程节点枚举
 */
public enum FlowServiceNodeEnum {

    authenticationSubmit("authenticationSubmit","实名认证提交","ServiceNode"),
    authenticationSuccess("authenticationSuccess","实名认证完成","ServiceNode"),

    loanApplicationSubmit("loanApplicationSubmit","融资申请提交","ServiceNode"),
    loanApplicationSuccess("loanApplicationSuccess","融资申请完成","ServiceNode"),
    contractSigningSubmit("contractSigningSubmit","合同签署提交","ServiceNode"),
    loanPayment("loanPayment","融资请款","ServiceNode"),
    loanSuccess("loanSuccess","合同签署完成","ServiceNode"),

    companyRiskLimitSubmit("companyRiskLimitSubmit","企业风控额度提交申请","ServiceNode"),
    companyRiskLimitSuccess("companyRiskLimitSuccess","企业风控额度申请完成","ServiceNode"),

    loanTurnStatusSubmit("loanTurnStatusSubmit","融资转追账/坏账申请提交","ServiceNode"),
    loanTurnStatusSuccess("loanTurnStatusSuccess","融资转追账/坏账申请完成","ServiceNode"),
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
