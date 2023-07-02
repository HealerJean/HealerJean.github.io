package com.healerjean.proj.statemechine.exception;

import com.healerjean.proj.statemechine.context.StateContext;

/**
 * OrderStateMarchException
 *
 * @author zhangyujin
 * @date 2023/6/28$  11:49$
 */
public class OrderStateMarchException extends RuntimeException {

    private String code;

    private String message;

    private StateContext context;

    public OrderStateMarchException(StateContext context) {
        this.context = context;
    }

    public OrderStateMarchException(String message, StateContext context) {
        super(message);
        this.message = message;
        this.context = context;
    }

    public OrderStateMarchException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public OrderStateMarchException(String code, String message, StateContext context) {
        super(message);
        this.code = code;
        this.message = message;
        this.context = context;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StateContext getContext() {
        return context;
    }

    public void setContext(StateContext context) {
        this.context = context;
    }
}
