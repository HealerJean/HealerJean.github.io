package com.healerjean.proj.common.exception;

import com.healerjean.proj.common.enums.ResponseEnum;

/**
 * @author HealerJean
 * @ClassName ParameterErrorException
 * @date 2019/10/17  16:19.
 * @Description 参数错误
 */
public class ParameterErrorException extends BusinessException {

    public ParameterErrorException() {
        super(ResponseEnum.参数错误);
    }

    public ParameterErrorException(ResponseEnum responseEnum) {
        super(ResponseEnum.参数错误, responseEnum.msg);
    }

    public ParameterErrorException(String msg) {
        super(ResponseEnum.参数错误, msg);
    }


}
