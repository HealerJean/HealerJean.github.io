package com.healerjean.proj.study.spi;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HealerJean
 * @ClassName Elephant
 * @date 2020/6/28  10:10.
 * @Description
 */
// @Adaptive
@Slf4j
public class AdaptiveAnimal implements Animal {


    @Override
    public void call(String msg, URL url) {
      log.info("{}，我可以发出任何叫声", msg);
    }

    @Override
    public void eat(String msg) {

    }

}
