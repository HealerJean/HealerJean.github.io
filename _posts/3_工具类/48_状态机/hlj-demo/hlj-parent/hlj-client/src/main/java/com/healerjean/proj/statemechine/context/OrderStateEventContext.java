package com.healerjean.proj.statemechine.context;

import com.healerjean.proj.statemechine.enent.OrderEventEnum;
import com.healerjean.proj.statemechine.statue.OrderStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OrderStateEventContext
 *
 * @author zhangyujin
 * @date 2023/6/28$  11:29$
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderStateEventContext extends StateEventContext<OrderStatusEnum, OrderEventEnum> {


    /**
     * 订单号
     */
    private String orderId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 订单事件
     */
    private OrderEventEnum orderEventEnum;


    @Override
    public Object fetchContext() {
        return this;
    }

    @Override
    protected OrderEventEnum fetchEvent() {
        return this.orderEventEnum;
    }
}
