package com.healerjean.proj.strata.app.item.api.impl;

import com.healerjean.proj.strata.app.item.converter.ItemBOConverter;
import com.healerjean.proj.strata.client.item.api.ItemService;
import com.healerjean.proj.strata.client.item.dto.ItemDTO;
import com.healerjean.proj.strata.client.item.param.ItemParam;
import com.healerjean.proj.strata.domain.item.bo.ItemBO;
import com.healerjean.proj.strata.domain.item.service.ItemDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemDomainService itemDomainService;

    @Override
    public ItemDTO sayHello(ItemParam param) {
        log.info("sayHello {}", param.getReq());
        ItemBO itemBO = itemDomainService.doSomething(null, null);
        return ItemBOConverter.convert(itemBO);
    }
}
