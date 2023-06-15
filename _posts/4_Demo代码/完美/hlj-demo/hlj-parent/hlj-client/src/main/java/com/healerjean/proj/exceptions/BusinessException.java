package com.healerjean.proj.exceptions;


import com.healerjean.proj.common.enums.CodeEnum;

/**
 * 业务异常 BusinessException
 *
 * @author zhangyujin
 * @date 2023/5/22  17:02.
 */
public class BusinessException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 799633539625676004L;

    /**
     * 返回错误码
     */
    private  String code;

    /**
     * 展示信息
     */
    private  String showMsg;


    /**
     * BusinessException
     *
     * @param businessErrorEnum businessErrorEnum
     */
    public BusinessException(CodeEnum.BusinessErrorEnum businessErrorEnum) {
        super(businessErrorEnum.getMsg());
        this.code = businessErrorEnum.getCode();
        this.showMsg = businessErrorEnum.getShowMsg();

    }

    /**
     * BusinessException
     *
     * @param message message
     */
    public BusinessException(String message) {
        super(message);
        this.code = CodeEnum.ErrorCodeEnum.ERROR_CODE_BUSINESS_ERROR.getCode();
        this.showMsg = message;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShowMsg() {
        return showMsg;
    }

    public void setShowMsg(String showMsg) {
        this.showMsg = showMsg;
    }
}
