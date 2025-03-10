package com.healerjean.proj.exceptions;

import com.healerjean.proj.common.enums.CodeEnum;
import lombok.Getter;

/**
 * PlatformException
 *
 * @author zhangyujin
 * @date 2024/1/3
 */
@Getter
public class PlatformException extends RuntimeException {

    @SuppressWarnings("all")
    private static final long serialVersionUID = 5535821215702463243L;

    /**
     * 返回错误码
     */
    private final String code;

    /**
     * 展示信息
     */
    private final String showMsg;

    /**
     * PlatformException
     *
     * @param platformErrorEnum platformErrorEnum
     */
    public PlatformException(CodeEnum.PlatformErrorEnum platformErrorEnum) {
        super(platformErrorEnum.getMsg());
        this.code = platformErrorEnum.getCode();
        this.showMsg =  CodeEnum.ErrorCodeEnum.ERROR_CODE_FAIL.getMsg();
    }


    /**
     * PlatformException
     *
     * @param platformErrorEnum platformErrorEnum
     */
    public PlatformException(CodeEnum.PlatformErrorEnum platformErrorEnum, Throwable e) {
        super(e.getMessage(), e);
        this.code = platformErrorEnum.getCode();
        this.showMsg = CodeEnum.ErrorCodeEnum.ERROR_CODE_FAIL.getMsg();
    }


    /**
     * PlatformException
     *
     * @param platformErrorEnum platformErrorEnum
     */
    public PlatformException(CodeEnum.PlatformErrorEnum platformErrorEnum, String showMessage) {
        super(platformErrorEnum.getMsg());
        this.code = platformErrorEnum.getCode();
        this.showMsg = showMessage;
    }


}
