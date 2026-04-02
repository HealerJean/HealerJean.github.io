package com.healerjean.proj.strata.client.order.api;

import com.healerjean.proj.strata.client.order.dto.OrderDTO;
import com.healerjean.proj.strata.client.order.param.OrderParam;

/**
 * Description: RPC
 */
public interface OrderService {


    OrderDTO getFromDb(OrderParam param);

}
