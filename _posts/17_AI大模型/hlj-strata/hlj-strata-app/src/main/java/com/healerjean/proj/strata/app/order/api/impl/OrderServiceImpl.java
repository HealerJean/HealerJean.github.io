package com.healerjean.proj.strata.app.order.api.impl;

import com.healerjean.proj.strata.app.order.converter.OrderBOConverter;
import com.healerjean.proj.strata.client.order.api.OrderService;
import com.healerjean.proj.strata.client.order.dto.OrderDTO;
import com.healerjean.proj.strata.client.order.param.OrderParam;
import com.healerjean.proj.strata.domain.order.bo.OrderBO;
import com.healerjean.proj.strata.domain.order.service.OrderDomainService;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDomainService orderDomainService;

    @Override
    public OrderDTO getFromRpc(OrderParam param) {
        log.info("orderservice getFromRpc {}", param.getId());
        OrderBO orderBO = orderDomainService.getFromRpc(param.getId());
        return OrderBOConverter.convert(orderBO);
    }


    @Override
    public OrderDTO getFromDb(OrderParam param) {
        log.info("orderservice getFromDb {}", param.getId());
        OrderBO orderBO = orderDomainService.getFromDb(param.getId());
        return OrderBOConverter.convert(orderBO);
    }






}
