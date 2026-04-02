package com.healerjean.proj.strata.app.order.api.impl;

import com.healerjean.proj.strata.app.order.converter.OrderBOConverter;
import com.healerjean.proj.strata.client.order.api.OrderService;
import com.healerjean.proj.strata.client.order.dto.OrderDTO;
import com.healerjean.proj.strata.client.order.param.OrderParam;
import com.healerjean.proj.strata.domain.order.bo.OrderBO;
import com.healerjean.proj.strata.domain.order.service.OrderDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderDomainService orderDomainService;


    @Override
    public OrderDTO getFromDb(OrderParam param) {
        log.info("orderservice getFromDb {}", param.getId());
        OrderBO orderBO = orderDomainService.getFromDb(param.getId());
        return OrderBOConverter.convert(orderBO);
    }

}
