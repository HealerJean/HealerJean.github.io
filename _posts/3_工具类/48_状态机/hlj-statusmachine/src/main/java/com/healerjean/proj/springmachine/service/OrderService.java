package com.healerjean.proj.service;


import com.healerjean.proj.entity.Order;

/**
 * 订单服务
 *
 * @author sunbufu
 */
public interface OrderService {

    /**
     * 创建
     * 
     * @return
     */
    Order create();

    /**
     * 支付
     * 
     * @param id
     * @return
     */
    Order pay(int id);

    /**
     * 发货
     * 
     * @param id
     * @return
     */
    Order deliver(int id);

    /**
     * 收货
     * 
     * @param id
     * @return
     */
    Order receive(int id);

}
