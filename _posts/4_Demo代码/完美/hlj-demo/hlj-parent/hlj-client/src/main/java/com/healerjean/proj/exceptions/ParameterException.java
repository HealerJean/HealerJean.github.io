package com.healerjean.proj.exceptions;

import com.healerjean.proj.common.enums.CodeEnum;
import lombok.Getter;

/**
 * ParameterErrorException
 *
 * @author zhangyujin
 * @date 2023/6/15  10:34.
 */
@Getter
public class ParameterException extends RuntimeException {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6114625076221233075L;
    /**
     * 返回错误码
     */
    private final String code;


    /**
     * BusinessException
     *
     * @param paramErrorEnum paramErrorEnum
     */
    public ParameterException(CodeEnum.ParamsErrorEnum paramErrorEnum) {
        super(paramErrorEnum.getMsg());
        this.code = paramErrorEnum.getCode();
    }

    /**
     * ParameterErrorException
     *
     * @param message message
     */
    public ParameterException(String message) {
        super(message);
        this.code = CodeEnum.ErrorCodeEnum.ERROR_CODE_PARAMS_ERROR.getCode();
    }

}
