package com.healerjean.common;


/**
 * Created by j.sh on 2015/5/9.
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    private String code;

    public BusinessException(String message) {
        super(message);
        this.code = ErrorCodeEnum.逻辑错误.code;
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
