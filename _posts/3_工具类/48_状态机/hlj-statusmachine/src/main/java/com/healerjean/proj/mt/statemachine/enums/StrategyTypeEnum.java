package com.healerjean.proj.mt.statemachine.enums;

/**
 * @author zhangyujin
 * @date 2021/3/5  5:25 下午.
 * @description 策略类型枚举
 */
public enum StrategyTypeEnum {

    USER_STRATEGY("UserStrategy", "用户策略"),
    COMMAND_EXPIRE_STRATE("CommandExpireStrate", "过期策略"),;

    private String name;

    private String desc;

    StrategyTypeEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
    }
