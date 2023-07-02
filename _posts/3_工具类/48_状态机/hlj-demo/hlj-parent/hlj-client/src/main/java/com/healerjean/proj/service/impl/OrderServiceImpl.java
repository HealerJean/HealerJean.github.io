package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.data.bo.OrderBO;
import com.healerjean.proj.service.OrderService;
import com.healerjean.proj.statemechine.context.OrderStateEventContext;
import com.healerjean.proj.statemechine.enums.EventExecuteResultEnum;
import com.healerjean.proj.statemechine.impl.OrderStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OrderService
 * @author zhangyujin
 * @date 2023/6/28$  12:51$
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    /**
     * orderStateMachine
     */
    @Resource
    private OrderStateMachine orderStateMachine;

    /**
     * OrderService
     * @param order order
     * @return boolean
     */
    @LogIndex
    @Override
    public boolean sendEvent(OrderBO order) {
        OrderStateEventContext orderStateEventContext = new OrderStateEventContext();
        orderStateEventContext.setOrderId(order.getOrderId());
        orderStateEventContext.setUserId(order.getUserId());
        orderStateEventContext.setOrderEventEnum(order.getEventEnum());
        EventExecuteResultEnum eventExecuteResultEnum = orderStateMachine.invokeSendEvent(orderStateEventContext);
        return eventExecuteResultEnum.getResult();
    }
}
