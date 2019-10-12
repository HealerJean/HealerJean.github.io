package com.hlj.proj.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName ResponseEnum
 * @date 2019/6/13  20:45.
 * @msgcription
 */
public enum ResponseEnum   {


   正常(200,"访问正常"),

    参数错误(351,"参数错误"),
    参数格式异常(352, "参数格式异常"),
    逻辑错误(353,"逻辑错误"),

    请求无法被服务器理解(400,"请求无法被服务器理解"),
    未授权(401, "未授权"),
    访问禁止(403,"访问禁止"),
    页面丢失(404,"页面丢失"),


    系统错误(500,"系统错误"),
    未知错误(999,"未知错误"),


     ;


    public int code;
    public String msg;

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }



    public static boolean checkExist( Integer code){
        for (ResponseEnum value : ResponseEnum.values()){
            if (value.code == code){
                return true;
            }
        }
        return false;
    }


    public static ResponseEnum getStatus(int code){
        for (ResponseEnum value : ResponseEnum.values()){
            if (value.code == code){
                return value;
            }
        }
        return ResponseEnum.未知错误;
    }

    public static String getMsg(int code){
        for (ResponseEnum value : ResponseEnum.values()){
            if (value.code == code){
                return value.msg;
            }
        }
        return ResponseEnum.未知错误.msg;
    }


    public static List<ResponseEnum> getList(){
        return Arrays.asList(values());
    }


}
