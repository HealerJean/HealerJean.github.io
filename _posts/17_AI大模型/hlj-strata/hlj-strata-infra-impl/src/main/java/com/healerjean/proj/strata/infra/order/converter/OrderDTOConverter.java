package com.healerjean.proj.strata.infra.order.converter;

import com.healerjean.proj.strata.client.order.dto.OrderDTO;
import com.healerjean.proj.strata.infra.order.dataobject.OrderDO;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class OrderDTOConverter {
    public static OrderDO convert(OrderDTO orderDTO) {
        OrderDO orderDO = new OrderDO();
        orderDO.setId(orderDTO.getId());
        orderDO.setValue(orderDTO.getValue());
        return orderDO;
    }
}
