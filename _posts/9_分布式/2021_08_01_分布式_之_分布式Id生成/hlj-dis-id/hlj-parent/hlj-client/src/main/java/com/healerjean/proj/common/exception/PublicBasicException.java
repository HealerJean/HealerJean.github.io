package com.healerjean.proj.common.exception;


public class PublicBasicException extends RuntimeException {

    /**
     * 返回错误码
     */
    private int code;
    /**
     * 返回错误信息
     */
    private String msg;
    /**
     * 异常信息
     */
    private Throwable e;

    public PublicBasicException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public PublicBasicException(int code, String msg, Throwable e) {
        this.code = code;
        this.msg = msg;
        this.e = e;
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
