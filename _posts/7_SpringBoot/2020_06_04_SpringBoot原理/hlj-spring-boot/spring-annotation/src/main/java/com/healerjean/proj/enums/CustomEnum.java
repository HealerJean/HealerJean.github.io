package com.healerjean.proj.enums;

/**
 * @author zhangyujin
 * @date 2022/4/13  21:26.
 * @description
 */
public enum CustomEnum {

    ONE("one", "一"),;

    private String code;
    private String desc;

    CustomEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
