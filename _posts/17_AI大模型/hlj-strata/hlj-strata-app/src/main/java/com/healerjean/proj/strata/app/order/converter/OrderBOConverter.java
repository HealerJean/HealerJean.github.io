package com.healerjean.proj.strata.app.order.converter;

import com.healerjean.proj.strata.client.order.dto.OrderDTO;
import com.healerjean.proj.strata.domain.order.bo.OrderBO;

public class OrderBOConverter {

    public static OrderDTO convert(OrderBO orderBO) {
        OrderDTO orderDTO = new OrderDTO();
        if (orderBO != null) {
            orderDTO.setId(orderBO.getId());
            orderDTO.setValue(orderBO.getValue());
        }
        return orderDTO;
    }
}
