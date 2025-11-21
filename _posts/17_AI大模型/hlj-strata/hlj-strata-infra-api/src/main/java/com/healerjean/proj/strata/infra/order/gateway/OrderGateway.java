package com.healerjean.proj.strata.infra.order.gateway;

import com.healerjean.proj.strata.infra.order.dataobject.OrderDO;

public interface OrderGateway {

    OrderDO getByIdFromDb(String orderId);
    OrderDO getByIdFromRpc(String orderId);
}
