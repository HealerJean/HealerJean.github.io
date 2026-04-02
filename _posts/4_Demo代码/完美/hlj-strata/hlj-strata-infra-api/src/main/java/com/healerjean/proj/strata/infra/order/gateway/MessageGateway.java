package com.healerjean.proj.strata.infra.order.gateway;


import com.healerjean.proj.strata.infra.order.dataobject.OrderDO;

public interface MessageGateway {

    boolean sendMessage(OrderDO orderDO);
}
