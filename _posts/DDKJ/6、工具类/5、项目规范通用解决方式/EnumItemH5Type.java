package com.duodian.youhui.enums.coupon;


import java.util.Arrays;
import java.util.List;

/**
 * @Desc: 推广位类型
 * @Date:  2018/6/1 上午10:09.
 */

public enum EnumItemH5Type {

    今日推送(1,"今日推送"),
    首页推荐(2,"首页推荐"),
    人气排行(3,"人气排行"),
    品牌馆(4,"品牌馆"),
    夜市疯抢(5,"夜市疯抢"),
    _99包邮(6,"_99包邮"),
    新人免单(7,"新人免单"),
    商城搜索(8,"商城搜索"),
    文章扫码(9,"文章扫码"),


    未知(99,"未知")
    ;

    public Integer status;
    public String des;

    EnumItemH5Type(Integer status, String des) {
        this.status = status;
        this.des = des;
    }


    public Integer getStatus() {
        return status;
    }

    public String getDes() {
        return des;
    }

    public static boolean checkExist(Integer source){
        for (EnumItemH5Type value : EnumItemH5Type.values()){
            if (value.status == source){
                return true;
            }
        }
        return false;
    }



    public static EnumItemH5Type getStatus(Integer status){
        for (EnumItemH5Type value : EnumItemH5Type.values()){
            if (value.status == status){
                return value;
            }
        }
        return EnumItemH5Type.未知;
    }

    public static String getDes(Integer status){
        for (EnumItemH5Type value : EnumItemH5Type.values()){
            if (value.status == status){
                return value.des;
            }
        }
        return EnumItemH5Type.未知.des;
    }

    public static List<EnumItemH5Type> getTypeList(){
        return Arrays.asList(values());
    }


}
