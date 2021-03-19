package com.healerjean.proj.springmachine.service.impl;

import com.healerjean.proj.springmachine.entity.Order;
import com.healerjean.proj.springmachine.entity.OrderEvent;
import com.healerjean.proj.springmachine.entity.OrderState;
import com.healerjean.proj.springmachine.mapper.OrderMapper;
import com.healerjean.proj.springmachine.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

/**
 * 订单服务
 * <p>
 * 待支付 -> 待发货 -> 待收货 -> 完结
 *
 * @author sunbufu
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StateMachine<OrderState, OrderEvent> orderStateMachine;

    @Autowired
    private StateMachinePersister<OrderState, OrderEvent, Order> orderStateMachinePersister;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order create() {
        Order order = new Order();
        order.setStatus(OrderState.WAIT_PAYMENT);
        return orderMapper.save(order);
    }

    @Override
    public Order pay(int id) {
        Order order = orderMapper.select(id);
        if (!sendEvent(OrderEvent.PAYED, order)) {
            log.error(" 等待支付 -> 等待发货 失败, 状态异常 order {}", order);
            throw new RuntimeException(" 等待支付 -> 等待发货 失败, 状态异常 order=" + order);
        }
        return order;
    }

    @Override
    public Order deliver(int id) {
        Order order = orderMapper.select(id);
        if (!sendEvent(OrderEvent.DELIVERY, order)) {
            log.error(" 等待发货 -> 等待收货 失败，状态异常 order {}", order);
            throw new RuntimeException(" 等待发货 -> 等待收货 失败，状态异常 order=" + order);
        }
        return order;
    }

    @Override
    public Order receive(int id) {
        Order order = orderMapper.select(id);
        if (!sendEvent(OrderEvent.RECEIVED, order)) {
            log.error(" 等待收货 -> 完成 失败，状态异常 order {}", order);
            throw new RuntimeException(" 等待收货 -> 完成 失败，状态异常 order=" + order);
        }
        return order;
    }

    /**
     * 发送订单状态转换事件
     *
     * @param event 事件
     * @param order 订单
     * @return 执行结果
     */
    private boolean sendEvent(OrderEvent event, Order order) {
        boolean result = false;
        try {
            orderStateMachine.start();
            // 读取状态机最新状态
            orderStateMachinePersister.restore(orderStateMachine, order);
            Message<OrderEvent> messageOrderEvent = MessageBuilder.withPayload(event).setHeader("order", order).build();
            result = orderStateMachine.sendEvent(messageOrderEvent);
            // 保存状态机最新状态
            orderStateMachinePersister.persist(orderStateMachine, order);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return result;
    }
}
