package com.healerjean.proj.strata.infra.order.gateway.impl;

import com.healerjean.proj.strata.infra.order.dataobject.OrderDO;
import com.healerjean.proj.strata.infra.order.gateway.OrderGateway;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class OrderGatewayImpl implements OrderGateway {

    private static final Logger log = LoggerFactory.getLogger(OrderGatewayImpl.class);


    @Override
    public OrderDO getByIdFromDb(String orderId) {
        OrderDO order = new OrderDO();
        order.setId("88888");
        order.setValue("healerjean");
        return order;
    }

    @Override
    public OrderDO getByIdFromRpc(String orderId) {
        return getByIdFromDb(orderId);
    }
}
