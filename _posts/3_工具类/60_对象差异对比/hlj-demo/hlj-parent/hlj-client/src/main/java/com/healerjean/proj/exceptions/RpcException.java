package com.healerjean.proj.exceptions;

import com.healerjean.proj.common.enums.CodeEnum;

/**
 * RpcException
 *
 * @author zhangyujin
 * @date 2023/6/15  10:37.
 */
public class RpcException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 799633539625676004L;

    /**
     * 返回错误码
     */
    private  String code;


    /**
     * BusinessException
     *
     * @param rpcErrorEnum rpcErrorEnum
     */
    public RpcException(CodeEnum.RpcErrorEnum rpcErrorEnum) {
        super(rpcErrorEnum.getMsg());
        this.code = rpcErrorEnum.getCode();
    }

    /**
     * BusinessException
     *
     * @param message message
     */
    public RpcException(String message) {
        super(message);
        this.code = CodeEnum.ErrorCodeEnum.ERROR_CODE_PRC_ERROR.getCode();
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}