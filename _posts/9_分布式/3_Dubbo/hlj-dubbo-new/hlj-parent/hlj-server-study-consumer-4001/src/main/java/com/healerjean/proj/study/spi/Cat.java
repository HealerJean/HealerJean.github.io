package com.healerjean.proj.study.spi;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;

/**
 * @author HealerJean
 * @ClassName Cat
 * @date 2020/6/24  18:41.
 * @Description
 */
@Slf4j
public class Cat implements Animal {

    private Animal animal;

    public Cat() {
    }

    public Cat(Animal animal) {
        animal = animal;
    }

    @Override
    public void call(String msg, URL url) {
        log.info("我是猫，发出叫声： {},", msg);
    }

}
