package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.bizlog.data.po.Order;
import com.healerjean.proj.service.OrderQueryService;
import com.healerjean.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * OrderQueryServiceImpl
 *
 * @author zhangyujin
 * @date 2023/5/31  15:55
 */
@Slf4j
@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    /**
     * 查询订单信息
     *
     * @param orderId orderId
     * @return Order
     */
    @Override
    public Order queryOrder(long orderId) {
        Order order = new Order();
        order.setProductName("大疆飞机");

        log.info("[OrderQueryService#queryOrder] orderId:{}, order:{}", orderId, JsonUtils.toJsonString(order));
        return order;
    }
}
