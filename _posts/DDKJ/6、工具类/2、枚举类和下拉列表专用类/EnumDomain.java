package com.duodian.youhui.enums;


import org.apache.commons.lang3.StringUtils;

/**
 * @Desc: 系统域名
 */

public enum EnumDomain {

    转链接中间系统域名("转链接中间系统域名","转链接中间系统域名"),
    前端调用服务端域名("前端调用服务端域名","前端调用服务端域名"),

    优惠直播("优惠直播","优惠直播"),
    前端域名("前端域名","前端域名"),
    微信前端域名("微信前端域名","微信前端域名"),
    H5前端域名("H5前端域名","H5前端域名"),

    未知("99","未知错误")
    ;

    public String code;
    public String des;

    EnumDomain(String code, String des) {
        this.code = code;
        this.des = des;
    }


    public String getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }

    public static boolean checkExist(String code){
        if(StringUtils.isBlank(code)) return false;
        for (EnumDomain value : EnumDomain.values()){
            if (value.code.equals(code)){
                return true;
            }
        }
        return false;
    }


    public static EnumDomain getEnumPayErrorType(String code){
        if(StringUtils.isBlank(code)) return EnumDomain.未知;
        for (EnumDomain value : EnumDomain.values()){
            if (value.code.equals(code)){
                return value;
            }
        }
        return EnumDomain.未知;
    }

    public static String getDes(String code){
        if(StringUtils.isBlank(code)) return EnumDomain.未知.des;
        for (EnumDomain value : EnumDomain.values()){
            if (value.code.equals(code)){
                return value.des;
            }
        }
        return EnumDomain.未知.des;
    }
}
