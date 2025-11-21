package com.healerjean.proj.strata.client.order.api;

import com.healerjean.proj.strata.client.order.param.OrderParam;
import com.healerjean.proj.strata.client.order.dto.OrderDTO;

/**
 * Description: JSF Demo
 *
 * @date 2024-11-29
 */
public interface OrderService {

    OrderDTO getFromRpc(OrderParam param);

    OrderDTO getFromDb(OrderParam param);







}
