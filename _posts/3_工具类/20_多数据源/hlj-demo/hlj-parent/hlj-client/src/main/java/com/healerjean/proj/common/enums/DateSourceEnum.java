package com.healerjean.proj.common.enums;

/**
 * DateSourceEnum
 *
 * @author zhangyujin
 * @date 2023/6/15  14:37.
 */
public enum DateSourceEnum {

    HEALER_JEAN("healerjean", "主数据源"),
    TEST("test", "test数据源"),
    ;


    /**
     * code
     */
    private final String code;

    /**
     * desc
     */
    private final String desc;


    /**
     * DateSourceEnum
     *
     * @param code code
     * @param desc desc
     */
    DateSourceEnum(String code, String desc) {
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
