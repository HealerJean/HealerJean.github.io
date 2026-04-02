package com.healerjean.proj.strata.domain.order.service;

import com.healerjean.proj.strata.domain.order.bo.OrderBO;
import com.healerjean.proj.strata.domain.order.converter.OrderDOConverter;
import com.healerjean.proj.strata.infra.order.dataobject.OrderDO;
import com.healerjean.proj.strata.infra.order.gateway.OrderGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDomainService {


    @Autowired
    private OrderGateway orderGateway;



    public OrderBO getFromDb(String orderId) {
        OrderDO orderDO = orderGateway.getByIdFromDb(orderId);
        return OrderDOConverter.convert(orderDO);
    }


}
