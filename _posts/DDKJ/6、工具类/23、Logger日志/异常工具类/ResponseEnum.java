package com.hlj.util.Z010异常;

/**
 * 类描述：错误状态枚举值
 *
 * @version 1.0.0
 */
public enum ResponseEnum {

    成功(200, "成功"),
    参数错误(400, "参数错误"),
    逻辑错误(301,"逻辑错误"),

    ;
    private int code;
    private String msg;

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
 
}
