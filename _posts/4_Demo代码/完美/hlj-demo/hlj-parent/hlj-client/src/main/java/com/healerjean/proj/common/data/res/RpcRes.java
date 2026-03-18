package com.healerjean.proj.common.data.res;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * RpcRes
 *
 * @author zhangyujin
 * @date 2026/1/27
 */
@Accessors(chain = true)
@Data
public class RpcRes<T> {

    /**
     * 返回结果
     */
    private T data;

    /**
     * msg
     */
    private String msg;

    /**
     * Code
     */
    private String code;

    /**
     * 异常
     */
    private Exception e;

    public static <T> RpcRes<T> instanceOf(){
        RpcRes<T> rpcRes = new RpcRes<>();
        return rpcRes;
    }

}
