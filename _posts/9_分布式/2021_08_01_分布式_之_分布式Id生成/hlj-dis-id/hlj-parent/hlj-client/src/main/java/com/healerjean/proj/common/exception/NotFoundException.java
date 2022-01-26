package com.healerjean.proj.common.exception;


import com.healerjean.proj.common.enums.ResponseEnum;


public class NotFoundException extends BusinessException {

    public NotFoundException(ResponseEnum responseEnum) {
        super(responseEnum);
    }

}
