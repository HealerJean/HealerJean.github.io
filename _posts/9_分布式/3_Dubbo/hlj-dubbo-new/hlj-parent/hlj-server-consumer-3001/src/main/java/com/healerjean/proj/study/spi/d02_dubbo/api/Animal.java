package com.healerjean.proj.study.spi.d02_dubbo.api;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

@SPI("adaptiveAnimal")
public interface Animal {

    @Adaptive(value = {"aname", "bname"})
    void call(String msg, URL url);

}
