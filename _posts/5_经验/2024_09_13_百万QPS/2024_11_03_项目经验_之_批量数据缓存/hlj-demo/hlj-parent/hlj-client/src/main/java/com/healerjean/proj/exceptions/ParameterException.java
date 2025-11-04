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
    @SuppressWarnings("all")
    private static final long serialVersionUID = -6114625076221233075L;

    /**
     * 返回错误码
     */
    private final String code;

    /**
     * ParameterErrorException
     *
     * @param showMessage message
     */
    public ParameterException(String showMessage) {
        super(showMessage);
        this.code = CodeEnum.ParamsErrorEnum.ERROR_CODE_100000.getCode();
    }

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
     * BusinessException
     *
     * @param paramErrorEnum paramErrorEnum
     */
    public ParameterException(CodeEnum.ParamsErrorEnum paramErrorEnum, String showMessage) {
        super(showMessage);
        this.code = paramErrorEnum.getCode();
    }

}
