package com.duodian.admore.enums.workflow;


import java.util.Arrays;
import java.util.List;

/**
 * 类名称：EnumWorkflowTaskType
 * 类描述：提交的审核类型
 * 创建人：shiwei
 * 修改人：
 * 修改时间：2015/12/19 17:47
 * 修改备注：
 *
 * @version 1.0.0
 */
public enum EnumWorkflowTaskType {

    预开发票申请(1,"预开发票申请"),
    特批额度申请(2,"特批额度申请"),
    特批单价申请(3,"特批单价申请"),
    应用推广代金券申请(4,"应用推广代金券申请"),
    多点宝代金券申请(5,"多点宝代金券申请"),
    汇款底单审核(6,"汇款底单审核"),
    客户合同审核(7,"客户合同审核"),
    拜访客户审核(8,"拜访客户审核"),
    客户实名认证审核(9,"客户实名认证审核"),
    客户发票信息审核(10,"客户发票信息审核"),
    应用审核(11,"应用审核"),
    资源包退款审核(12,"资源包退款审核"),
    客户提现审核(13,"客户提现审核"),
    对接接口审核(14,"对接接口审核"),
    商务或运营变更审核(15,"商务或运营变更审核"),
    多点账户变更审核(16,"多点账户变更审核"),
    快启代金券申请(17,"快启代金券申请"),
    客户发票作废申请(18,"客户发票作废申请"),
    客户发票作废重开申请(19,"客户发票作废重开申请"),
    客户发票部分作废申请(20,"客户发票部分作废申请"),
    创建资源包价格范围申请(21,"创建资源包价格范围申请"),
    财务流水手动录入申请(22,"财务流水手动录入申请"),
    合同章申请(23,"合同章申请"),
    结算单盖章申请(24,"结算单盖章申请"),
    客户变更账号信息申请(25,"客户变更账号信息申请"),
    助手管理信息审核申请(26,"助手管理信息审核申请"), //张宇晋添加

    多点宝媒体审核(51,"多点宝媒体审核"),
    多点宝广告审核(52,"多点宝广告审核"),

    未知(99,"未知")
    ;

    public Integer type;
    public String des;

    EnumWorkflowTaskType(Integer type, String des) {
        this.type = type;
        this.des = des;
    }


    public Integer getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

    public static boolean checkExist(Integer type){
        if(type == null) return false;
        for (EnumWorkflowTaskType value : EnumWorkflowTaskType.values()){
            if (value.type.compareTo(type) == 0){
                return true;
            }
        }
        return false;
    }



    public static EnumWorkflowTaskType getEnumCashFlowType(Integer type){
        if(type == null) return EnumWorkflowTaskType.未知;
        for (EnumWorkflowTaskType value : EnumWorkflowTaskType.values()){
            if (value.type.compareTo(type) == 0){
                return value;
            }
        }
        return EnumWorkflowTaskType.未知;
    }

    public static String getDes(Integer type){
        if(type == null) return EnumWorkflowTaskType.未知.des;
        for (EnumWorkflowTaskType value : EnumWorkflowTaskType.values()){
            if (value.type.compareTo(type) == 0){
                return value.des;
            }
        }
        return EnumWorkflowTaskType.未知.des;
    }

    public static List<EnumWorkflowTaskType> getTypeList(){
        return Arrays.asList(values());
    }
}
