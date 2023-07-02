package com.healerjean.proj.service.impl;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.base.BaseJunit5SpringTest;
import com.healerjean.proj.data.bo.OrderBO;
import com.healerjean.proj.service.OrderService;
import com.healerjean.proj.statemechine.enent.OrderEventEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;


/**
 * @author zhangyujin
 * @date 2023/6/28$  12:54$
 */
@Slf4j
public class OrderServiceImplTest extends BaseJunit5SpringTest {

    /**
     * orderService
     */
    @Resource
    private OrderService orderService;

    @Test
    void sendEventSuccess() {
        OrderBO order = new OrderBO();
        order.setOrderId(""+System.currentTimeMillis());
        order.setUserId("8888");
        order.setEventEnum(OrderEventEnum.CREATE);
        boolean result = orderService.sendEvent(order);
        log.info("[OrderServiceImplTest#sendEventSuccess] result:{}", JSON.toJSONString(result));
    }

    @Test
    void sendEventFail() {
        OrderBO order = new OrderBO();
        order.setOrderId(""+System.currentTimeMillis());
        order.setUserId("8888");
        order.setEventEnum(OrderEventEnum.RECEIVED);
        boolean result = orderService.sendEvent(order);
        log.info("[OrderServiceImplTest#sendEventFail] result:{}", JSON.toJSONString(result));
    }

}