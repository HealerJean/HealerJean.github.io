package com.hlj.swagger.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel(description = "User内部对象")
public class Base {
    @ApiModelProperty(value = "baseId,比如：20")
    private int baseId;

    public int getBaseId() {
        return baseId;
    }

    public void setBaseId(int baseId) {
        this.baseId = baseId;
    }

    public Base() {
    }

    public Base(int baseId) {
        this.baseId = baseId;
    }
}
