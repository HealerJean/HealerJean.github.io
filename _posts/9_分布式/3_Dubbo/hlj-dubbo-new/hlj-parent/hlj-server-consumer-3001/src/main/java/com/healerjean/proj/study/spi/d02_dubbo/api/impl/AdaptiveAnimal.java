package com.healerjean.proj.study.spi.d02_dubbo.api.impl;

import com.healerjean.proj.study.spi.d02_dubbo.api.Animal;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;

@Adaptive
@Slf4j
public class AdaptiveAnimal implements Animal {

    @Override
    public void call(String msg, URL url) {
      log.info("我是适配：{}", msg);
    }
}
