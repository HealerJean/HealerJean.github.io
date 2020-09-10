package com.healerjean.proj.study.spi;


import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;

/**
 * @author HealerJean
 * @ClassName Dog
 * @date 2020/6/24  18:41.
 * @Description
 */
@Activate(group = "default_group", value = "valueAc")
@Slf4j
public class Dog implements Animal {

    @Override
    public void call(String msg, URL url) {
        log.info("我是狗，发出叫声： {},", msg);
    }

}
