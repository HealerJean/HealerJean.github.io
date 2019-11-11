package com.healerjean.proj.service.core.flow.enums;

/**
 * @author HealerJean
 * @ClassName FlowEnum
 * @date 2019-11-03  15:42.
 * @Description
 */
public interface FlowEnum {

    /**
     * 审批用户的类型
     */
    enum AuditTaskEnum {

        实名认证("Authentication", "实名认证"),
        融资申请("Loan", "融资申请"),
        分控额度申请("Risk", "分控额度申请"),

        ;
        public String code;
        public String desc;

        AuditTaskEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static AuditUserTypeEnum toEnum(String code) {
            for (AuditUserTypeEnum item : AuditUserTypeEnum.values()) {
                if (item.code.equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }



    /**
     * 审批用户的类型
     */
    enum AuditUserTypeEnum {

        审批人("Approver", "审批人"),
        抄送人("CopyUser", "抄送人"),
        ;
        public String code;
        public String desc;

        AuditUserTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static AuditUserTypeEnum toEnum(String code) {
            for (AuditUserTypeEnum item : AuditUserTypeEnum.values()) {
                if (item.code.equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }


    /**
     * 审批对象类型
     */
    enum AuditObjectTypeEnum {

        角色("Role", "角色"),
        用户("Id", "用户"),


        ;
        public String code;
        public String desc;

        AuditObjectTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static AuditObjectTypeEnum toEnum(String code) {
            for (AuditObjectTypeEnum item : AuditObjectTypeEnum.values()) {
                if (item.code.equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }




    /**
     * 审批状态
     */
    enum AuditStatusEnum {
        成功("10", "成功"),
        等待审批("20", "等待审批"),
        审批中("21", "审批中"),
        他人审批("22", "他人审批"),
        撤回审批("90", "撤回审批"),
        拒绝("99", "拒绝"),

        ;
        public String code;
        public String desc;

        AuditStatusEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }


    enum WorkStatusEnum {

        成功("10", "成功"),
        等待执行("20", "等待执行"),
        执行中("21", "执行中"),
        废弃("99", "废弃"),
        ;
        public String code;
        public String desc;

        WorkStatusEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    /**
     * 审批对象类型
     */
    enum FlowNodeTypeEnum {

        审批节点("AuditNode","审批节点"),
        业务节点("FlowWorkNodeService","业务节点");
        ;
        public String code;
        public String desc;

        FlowNodeTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static FlowNodeTypeEnum toEnum(String code) {
            for (FlowNodeTypeEnum item : FlowNodeTypeEnum.values()) {
                if (item.code.equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }


    /**
     * 流程节点枚举
     */
    enum FlowNodeEnum {

        融资申请("loanSubmit", "融资申请", "FlowWorkNodeService"),
        风控额度审批申请("riskLimitApproval", "风控额度审批", "AuditNode"),
        合同签署审批申请("contractSignApproval", "合同签署审批", "AuditNode"),
        融资出账("loanFund", "融资出账", "FlowWorkNodeService"),
        ;
        public String nodeCode;
        public String nodeName;
        public String nodeType;
        FlowNodeEnum(String nodeCode, String nodeName, String nodeType) {
            this.nodeCode = nodeCode;
            this.nodeName = nodeName;
            this.nodeType = nodeType;
        }
        public static FlowNodeEnum toEnum(String nodeCode) {
            for (FlowNodeEnum item : FlowNodeEnum.values()) {
                if (item.nodeCode.equals(nodeCode)) {
                    return item;
                }
            }
            return null;
        }
    }

}
