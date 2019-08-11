package com.hlj.proj.service.flow.service.enums;


/**
 * @author HealerJean
 * @ClassName FlowAuditNodeEnum
 * @Date 2019-08-11  18:48.
 * @Description 审批节点
 */

public enum FlowAuditNodeEnum {



    auditAJobDeal("auditAJobDeal","审核任务A处理"),
    auditBJobDeal("auditBJobDeal","审核任务B处理"),

    ;

    private String type;
    private String desc;

    FlowAuditNodeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static FlowAuditNodeEnum toEnum(String type) {
        for (FlowAuditNodeEnum item : FlowAuditNodeEnum.values()) {
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
