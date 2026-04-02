package com.healerjean.proj.strata.infra.order.gateway.impl;

import com.healerjean.proj.strata.infra.order.dataobject.OrderDO;
import com.healerjean.proj.strata.infra.order.gateway.OrderGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderGatewayImpl implements OrderGateway {

    @Override
    public OrderDO getByIdFromDb(String orderId) {
        return null;
    }



}
