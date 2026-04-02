package com.healerjean.proj.strata.domain.item.service;

import com.healerjean.proj.strata.domain.item.bo.ItemBO;
import com.healerjean.proj.strata.domain.item.converter.ItemDOConverter;
import com.healerjean.proj.strata.domain.order.bo.OrderBO;
import com.healerjean.proj.strata.infra.item.dataobject.ItemDO;
import com.healerjean.proj.strata.infra.item.gateway.ItemGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemDomainService {

    @Autowired
    private ItemGateway itemGateway;

    public ItemBO doSomething(OrderBO orderBO, ItemBO itemBO) {
        ItemDO itemDO = itemGateway.getById("1");
        return ItemDOConverter.convert(itemDO);
    }

}
