package com.healerjean.proj.common.exception;


import com.healerjean.proj.common.enums.ResponseEnum;


public class ExistException extends BusinessException {

    public ExistException(ResponseEnum responseEnum) {
        super(responseEnum);
    }

}
