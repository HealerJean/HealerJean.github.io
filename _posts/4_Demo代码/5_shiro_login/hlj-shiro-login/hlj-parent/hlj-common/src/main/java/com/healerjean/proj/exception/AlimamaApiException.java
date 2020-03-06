package com.healerjean.proj.exception;

import com.healerjean.proj.enums.ResponseEnum;

/**
 * @author HealerJean
 * @ClassName AlimamaApiException
 * @date 2019/10/15  12:31.
 * @Description
 */
public class AlimamaApiException extends BusinessException {


    public AlimamaApiException(String msg) {
        super(ResponseEnum.淘宝接口数据异常, msg);
    }

    public AlimamaApiException(Throwable e) {
        super(ResponseEnum.淘宝接口请求异常, e);
    }

}
