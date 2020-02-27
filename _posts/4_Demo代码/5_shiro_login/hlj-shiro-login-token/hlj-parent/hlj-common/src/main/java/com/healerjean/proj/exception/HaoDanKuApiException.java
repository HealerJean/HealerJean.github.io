package com.healerjean.proj.exception;

import com.healerjean.proj.enums.ResponseEnum;

/**
 * @author HealerJean
 * @ClassName HaoDanKuApiException
 * @date 2019/10/15  20:08.
 * @Description
 */
public class HaoDanKuApiException extends BusinessException {


    public HaoDanKuApiException( ) {
        super(ResponseEnum.好单库口请求异常);
    }

    public HaoDanKuApiException(String msg) {
        super(ResponseEnum.好单库接口数据异常, msg);
    }

    public HaoDanKuApiException(Throwable e) {
        super(ResponseEnum.好单库口请求异常, e);
    }

}
