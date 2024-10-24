package com.healerjean.proj.a_test.serializeable.base;

/**
 * SerializeException
 *
 * @author zhangyujin
 * @date 2024/10/22
 */

public class SerializeException extends RuntimeException {

    /**
     * SerializeException构造函数，使用给定的消息初始化异常
     *
     * @param message 异常消息
     */
    public SerializeException(String message) {
        super(message);
    }

    /**
     * 构造一个带指定详细消息和原因的新异常。
     *
     * @param message 详细消息描述
     * @param cause   异常原因
     */
    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }
}