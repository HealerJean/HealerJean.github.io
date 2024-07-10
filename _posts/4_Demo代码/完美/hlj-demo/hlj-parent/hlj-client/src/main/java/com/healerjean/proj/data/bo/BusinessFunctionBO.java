package com.healerjean.proj.data.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 批量消费
 *
 * @author zhangyujin
 * @date 2024/6/14
 */
@Data
public class BusinessFunctionBO<Req, Res> implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7922977872203781755L;

    /**
     * 请求
     */
    private Function<Req, Res> function;

    /**
     * 任务名
     */
    private String taskName;

    /**
     * 唯一id
     */
    private String uuid;

    /**
     * 请求对象
     */
    private Req req;


    /**
     * 返回 对象
     */
    private Res res;

    /**
     * 执行成功
     */
    private Boolean invokeFlag;

    /**
     * 执行异常信息
     */
    private Exception exception;
}


