package com.healerjean.proj.service;


import com.healerjean.proj.service.bizlog.data.po.Order;

/**
 * 订单 OrderQueryService
 */
public interface OrderQueryService {

    /**
     * 查询订单
     *
     * @param orderId orderId
     * @return Order
     */
    Order queryOrder(long orderId);
}
