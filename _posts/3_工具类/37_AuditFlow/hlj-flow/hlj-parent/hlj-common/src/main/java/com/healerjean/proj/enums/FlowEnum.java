package com.healerjean.proj.enums;

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
        等待审批("21", "等待审批"),
        审批中("22", "审批中"),
        他人审批("31", "他人审批"),
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







}
