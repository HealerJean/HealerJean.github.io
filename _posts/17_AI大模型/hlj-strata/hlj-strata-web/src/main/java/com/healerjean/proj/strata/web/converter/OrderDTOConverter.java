package com.healerjean.proj.strata.web.converter;

import com.healerjean.proj.strata.client.order.dto.OrderDTO;
import com.healerjean.proj.strata.web.vo.OrderVO;

public class OrderDTOConverter {
    public static OrderVO convert(OrderDTO orderDTO) {
        // convert DTO to VO
        OrderVO orderVO = new OrderVO();
        orderVO.setId(orderDTO.getId());
        orderVO.setValue(orderDTO.getValue());
        return orderVO;
    }
}
