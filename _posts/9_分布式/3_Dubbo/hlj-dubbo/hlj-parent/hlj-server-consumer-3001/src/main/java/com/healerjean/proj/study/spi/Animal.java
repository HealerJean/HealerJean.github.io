package com.healerjean.proj.study.spi;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

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
