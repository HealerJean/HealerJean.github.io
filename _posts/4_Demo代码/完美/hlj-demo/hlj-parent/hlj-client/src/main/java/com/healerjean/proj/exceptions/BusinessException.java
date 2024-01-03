package com.healerjean.proj.exceptions;


import com.healerjean.proj.common.enums.CodeEnum;
import lombok.Getter;

/**
 * 业务异常 BusinessException
 *
 * @author zhangyujin
 * @date 2023/5/22  17:02.
 */

@Getter
public class BusinessException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 799633539625676004L;

    /**
     * 返回错误码
     */
    private final String code;

    /**
     * 展示信息
     */
    private final String showMsg;


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

}
