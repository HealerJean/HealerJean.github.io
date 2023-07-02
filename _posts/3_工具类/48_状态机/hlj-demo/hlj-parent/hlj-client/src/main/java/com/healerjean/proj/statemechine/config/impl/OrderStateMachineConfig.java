package com.healerjean.proj.statemechine.config.impl;

import com.healerjean.proj.statemechine.config.EnableStateMachine;
import com.healerjean.proj.statemechine.config.IStateMachineConfig;
import com.healerjean.proj.statemechine.enent.OrderEventEnum;
import com.healerjean.proj.statemechine.statue.OrderStatusEnum;
import com.healerjean.proj.statemechine.transition.Transitions;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

/**
 * 订单状态机
 *
 * @author zhangyujin
 * @date 2023/6/28$  09:37$
 */
@EnableStateMachine("orderStateMachine")
@Configuration
public class OrderStateMachineConfig implements IStateMachineConfig<OrderStatusEnum, OrderEventEnum> {

    /**
     * configure
     *
     * @param transitions transitions
     */
    @Override
    public void configure(Transitions<OrderStatusEnum, OrderEventEnum> transitions) {

        // 订单状态：初始化  动作：创建订单 -> 订单状态：待支付
        transitions.withTransition()
                .source(EnumSet.of(OrderStatusEnum.INIT))
                .event(OrderEventEnum.CREATE)
                .target(OrderStatusEnum.WAIT_PAYMENT);

        // 订单状态：待支付  动作：支付 -> 订单状态：等待发货
        transitions.withTransition()
                .source(EnumSet.of(OrderStatusEnum.WAIT_PAYMENT))
                .event(OrderEventEnum.PAYED)
                .target(OrderStatusEnum.WAIT_DELIVER);

    }
}
