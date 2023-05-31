package com.healerjean.proj.service.impl;

import com.healerjean.proj.beans.Order;
import com.healerjean.proj.service.OrderQueryService;
import org.springframework.stereotype.Service;

/**
 * @author muzhantong
 * create on 2021/2/10 11:14 上午
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    @Override
    public Order queryOrder(long parseLong) {
        Order order = new Order();
        order.setProductName("xxxx");
        return order;
    }
}
