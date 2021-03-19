package com.healerjean.proj.springmachine.machine.listener;

import com.healerjean.proj.springmachine.entity.OrderEvent;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * 订单状态变更监听器
 * 
 * @author sunbufu
 */
@Component
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {

    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public void payTransition(Message<OrderEvent> message) {
        System.out.println("待支付 --支付--> 待发货");
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public void deliverTransition(Message<OrderEvent> message) {
        System.out.println("待发货 --发货--> 待收货");
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public void receiveTransition(Message<OrderEvent> message) {
        System.out.println("待收货 --收货--> 完成");
    }
}
