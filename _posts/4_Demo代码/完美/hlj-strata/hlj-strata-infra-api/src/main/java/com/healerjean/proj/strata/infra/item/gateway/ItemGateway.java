package com.healerjean.proj.strata.infra.item.gateway;


import com.healerjean.proj.strata.infra.item.dataobject.ItemDO;

public interface ItemGateway {
    ItemDO getById(String itemId);
}
