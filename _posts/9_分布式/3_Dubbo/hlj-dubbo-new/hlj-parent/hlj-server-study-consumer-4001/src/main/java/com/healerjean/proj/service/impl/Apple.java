package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.Fruits;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HealerJean
 * @ClassName MokeDubboService
 * @date 2020/6/24  17:04.
 * @Description
 */
@Slf4j
public class Apple implements Fruits {

    @Override
    public void name() {
        log.info("Fruits--------苹果");
    }

}
