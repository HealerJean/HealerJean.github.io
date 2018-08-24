package com.duodian.admore.enums.apps;


import org.apache.commons.lang.StringUtils;

/**
 * 类名称：EnumDockedParamValue
 * 类描述：内置系统变量
 * 创建人：qingxu
 * 修改人：
 * 修改时间：2015/12/19 17:47
 * 修改备注：
 *
 * @version 1.0.0
 */
public enum EnumDockedParamValue {

    APPID("${appid}","appId(1197828327)"),
    CLICKID("${clickId}","多点广告点击标识"),
    MAC格式一("${mac1}","MAC格式一(2C:20:0B:37:D3:1F)"),
    MAC格式二("${mac2}","MAC格式二(2c:20:0b:37:d3:1f)"),
    MAC格式三("${mac3}","MAC格式三(2c200b37d31f)"),
    MAC格式四("${mac4}","MAC格式四(2C200B37D31F)"),
    IDFA格式一("${idfa1}","IDFA格式一(D8C262D6-5B18-4289-BB28-CFE6DE1EDAAD)"),
    IDFA格式二("${idfa2}","IDFA格式二(D8C262D65B184289BB28CFE6DE1EDAAD)"),
    IDFA格式三("${idfa3}","IDFA格式三(d8c262d65b184289bb28cfe6de1edaad)"),
    IP地址("${ip}","ip地址(192.168.1.1)"),
    UDID("${udid}","udid(0013a499262748db89658a7d2175377a)"),
    iOS版本("${os}","ios版本(9.3)"),
    设备型号格式一("${device1}","设备型号(iPhone9,2)"),
    设备型号格式二("${device2}","设备型号(iPhone7 Plus)"),
    时间戳("${timestamp}","时间戳(自1970年以来的毫秒数)"),
    秒("${time1}","秒(自1970年以来的秒数)"),
    时间格式一("${date1}","yyyyMMddHHmmss"),
    时间格式二("${date2}","0时区时间戳(yyyy-MM-dd HH:mm:ss)"),
    回调接口格式一("${callback1}","回调接口格式一(UrlEncode)"),
    回调接口格式二("${callback2}","回调接口格式二(无需UrlEncode)"),
    回调接口格式三("${callback3}","回调接口格式三无IDFA参数(UrlEncode)"),
    回调接口格式四("${callback4}","回调接口格式四无IDFA参数(无需UrlEncode)"),
    签名("${sign}","加密的签名"),
    点击时间("${clicktime1}","点击时间"),
    通过时间("${passtime1}","通过时间"),




    未知("UNKNOWN","未知")
    ;

    public String type;
    public String des;

    EnumDockedParamValue(String type, String des) {
        this.type = type;
        this.des = des;
    }


    public String getType() {
        return type;
    }

    public String getDes() {
        return des;
    }

    public static boolean checkExist(String type){
        if(StringUtils.isBlank(type)) return false;
        for (EnumDockedParamValue value : EnumDockedParamValue.values()){
            if (value.type.equals(type)){
                return true;
            }
        }
        return false;
    }



    public static EnumDockedParamValue getEnumParamValue(String type){
        if(StringUtils.isBlank(type)) return EnumDockedParamValue.未知;
        for (EnumDockedParamValue value : EnumDockedParamValue.values()){
            if (value.type.equals(type)){
                return value;
            }
        }
        return EnumDockedParamValue.未知;
    }

    public static String getDes(String type){
        if(StringUtils.isBlank(type)) return EnumDockedParamValue.未知.des;
        for (EnumDockedParamValue value : EnumDockedParamValue.values()){
            if (value.type.equals(type)){
                return value.des;
            }
        }
        return EnumDockedParamValue.未知.des;
    }
}
