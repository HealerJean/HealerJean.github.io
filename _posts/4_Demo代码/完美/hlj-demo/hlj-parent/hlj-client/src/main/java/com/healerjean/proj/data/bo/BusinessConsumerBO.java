package com.healerjean.proj.data.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * 批量消费
 *
 * @author zhangyujin
 * @date 2024/6/14
 */
@Data
public class BusinessConsumerBO<R> implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7922977872203781755L;

    /**
     * 请求
     */
    private Consumer<R> consumer;

    /**
     * 消费任务名
     */
    private String taskName;

    /**
     * 请求对象
     */
    private R req;

    /**
     * 执行成功
     */
    private Boolean invokeFlag;


    /**
     * 执行异常信息
     */
    private Exception exception;
}


