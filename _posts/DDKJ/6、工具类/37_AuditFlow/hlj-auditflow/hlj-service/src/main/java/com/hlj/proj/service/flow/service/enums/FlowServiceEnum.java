package com.hlj.proj.service.flow.service.enums;

/**
 * @ClassName ServiceEnum
 * @Author TD
 * @Date 2019/6/13 10:08
 * @Description 流程节点枚举
 */
public enum  FlowServiceEnum {

    loanApplicationAutoFund("loanApplicationAutoFund","融资申请(自动请款)"),
    loanApplicationManualFund("loanApplicationManualFund","融资申请(非自动请款)"),
    toBadDebtOrChaseDebt("toBadDebtOrChaseDebt","转坏账/追账"),

    authentication("authentication","实名认证"),
    commpanyRiskLimitApplication("commpanyRiskLimitApplication","企业风控额度操作申请") ;


    private String flowCode;
    private String flowName;

    FlowServiceEnum(String flowCode, String flowName) {
        this.flowCode = flowCode;
        this.flowName = flowName;
    }

    public static FlowServiceEnum toEnum(String flowCode) {
        for (FlowServiceEnum item : FlowServiceEnum.values()) {
            if (item.getFlowCode().equals(flowCode)) {
                return item;
            }
        }
        return null;
    }


    public String getFlowCode() {
        return flowCode;
    }

    public void setFlowCode(String flowCode) {
        this.flowCode = flowCode;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}
