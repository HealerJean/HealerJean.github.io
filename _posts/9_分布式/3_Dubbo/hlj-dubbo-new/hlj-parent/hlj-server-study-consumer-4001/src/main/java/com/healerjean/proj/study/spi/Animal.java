package com.healerjean.proj.study.spi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * @author HealerJean
 * @ClassName Annoimal
 * @date 2020/6/24  18:41.
 * @Description
 */
@SPI
public interface Animal {

    @Adaptive(value = "name")
    void call(String msg, URL url);

}
