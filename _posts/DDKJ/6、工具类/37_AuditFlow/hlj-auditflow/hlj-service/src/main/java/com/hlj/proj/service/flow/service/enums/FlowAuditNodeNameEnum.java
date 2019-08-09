package com.hlj.proj.service.flow.service.enums;

/**
 * @ClassName FlowNodeNameEnum
 * @Author TD
 * @Date 2019/7/26 13:52
 * @Description 审批节点名称
 */
public enum  FlowAuditNodeNameEnum {

    loanApplicationApproval("loanApplicationApproval","新增融资申请"),
    contractApproval("contractApproval","合同签署申请"),
    fundApproval("fundApproval","融资申请出账"),
    chaseDebtApproval("chaseDebtApproval","转追账流程审核"),
    badDebtApproval("badDebtApproval","转坏账流程审核"),
    authentication("authentication","实名流程审核"),
    commpanyRiskLimitApplication("commpanyRiskLimitApplication","企业风控额度申请审批");

    private String type;
    private String desc;

    FlowAuditNodeNameEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static FlowAuditNodeNameEnum toEnum(String type) {
        for (FlowAuditNodeNameEnum item : FlowAuditNodeNameEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
