package com.healerjean.proj.statemechine.enent;

/**
 * OrderEventEnum
 *
 * @author zhangyujin
 * @date 2023/6/28$  11:02$
 */
public enum OrderEventEnum implements IEvent<OrderEventEnum> {

    CREATE("Create", "订单创建"),
    PAYED("payed", "支付"),
    DELIVERY("delivery", "发货"),
    RECEIVED("received", "收货"),

    ;


    private final String code;
    private final String desc;

    OrderEventEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


    @Override
    public OrderEventEnum getEvent() {
        return this;
    }
}
