package com.healerjean.proj.statemechine.statue;

/**
 * OrderStatusEnum
 *
 * @author zhangyujin
 * @date 2023/6/28$  10:58$
 */
public enum OrderStatusEnum implements IState<OrderStatusEnum> {

    INIT("init", "初始"),
    WAIT_PAYMENT("waitPayment", "待支付"),
    WAIT_DELIVER("waitDeliver", "待发货"),
    WAIT_RECEIVE("waitReceive", "待收货"),
    FINISH("finish", "完结"),
    ;


    private final String code;
    private final String desc;

    OrderStatusEnum(String code, String desc) {
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
    public OrderStatusEnum getState() {
        return this;
    }
}
