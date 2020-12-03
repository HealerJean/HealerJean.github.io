package com.hlj.moudle.design.D00_单例模式;

/**
 * @author HealerJean
 * @date 2020/12/3  10:29.
 * @description
 */
public enum  SingtetonEnum {

    EFFECT("10", "生效"),
    TRASH("99", "失败"),
        ;
     SingtetonEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    public static SingtetonEnum toEnum(String code) {
        for (SingtetonEnum item : SingtetonEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }
}
