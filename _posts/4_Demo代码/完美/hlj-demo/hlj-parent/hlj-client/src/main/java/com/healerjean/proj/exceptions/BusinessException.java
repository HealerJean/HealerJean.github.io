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


    @SuppressWarnings("all")
    private static final long serialVersionUID = 651986696176656087L;

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
     * @param businessErrorEnum businessErrorEnum
     */
    public BusinessException(CodeEnum.BusinessErrorEnum businessErrorEnum, Throwable e) {
        super(businessErrorEnum.getMsg(), e);
        this.code = businessErrorEnum.getCode();
        this.showMsg = businessErrorEnum.getShowMsg();
    }

    /**
     * BusinessException
     *
     * @param showMessage message
     */
    public BusinessException(CodeEnum.BusinessErrorEnum businessErrorEnum, String showMessage) {
        super(businessErrorEnum.getMsg());
        this.code = CodeEnum.BusinessErrorEnum.ERROR_CODE_300000.getCode();
        this.showMsg = showMessage;
    }

}
