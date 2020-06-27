package com.healerjean.proj.study.spi;

import com.alibaba.dubbo.common.URL;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HealerJean
 * @ClassName Cat
 * @date 2020/6/24  18:41.
 * @Description
 */
@Slf4j
public class Cat implements Annoimal {

    @Override
    public void call(URL url) {
        log.info("猫 call 喵喵");
    }
}
