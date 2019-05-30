package com.hlj.util.Z010异常;



/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    private int code;

    public BusinessException(int code) {
        this.code = code;
    }


    public BusinessException(String message) {
        super(message);
        this.code = ResponseEnum.逻辑错误.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResponseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.code = responseEnum.getCode() ;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResponseEnum.逻辑错误.getCode();
    }

    public BusinessException(int code ,Throwable e) {
        super(e);
        this.code = code;
    }


    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
