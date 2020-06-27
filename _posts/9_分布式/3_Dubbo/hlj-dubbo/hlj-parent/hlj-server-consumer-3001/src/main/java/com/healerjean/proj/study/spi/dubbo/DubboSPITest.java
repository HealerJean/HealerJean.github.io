package com.healerjean.proj.study.spi.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.healerjean.proj.service.Fruits;
import com.healerjean.proj.study.spi.Annoimal;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName DubboSPITest
 * @date 2020-06-26  16:03.
 * @Description
 */
@Slf4j
public class DubboSPITest {

    @Test
    public void load()  {
        ExtensionLoader<Annoimal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Annoimal.class);

        log.info("#######################################");
        Annoimal dog = annoimalExtensionLoader.getExtension("dog");
        dog.call(null);
        Annoimal cat = annoimalExtensionLoader.getExtension("cat");
        cat.call(null);
        log.info("#######################################");


        ExtensionLoader<Fruits> fruitsExtensionLoader =  ExtensionLoader.getExtensionLoader(Fruits.class);
        Fruits apple = fruitsExtensionLoader.getExtension("apple");
        apple.name();
    }


    @Test
    public void adaptive()  {
        ExtensionLoader<Annoimal> annoimalExtensionLoader =  ExtensionLoader.getExtensionLoader(Annoimal.class);
        Annoimal adaptiveExtension = annoimalExtensionLoader.getAdaptiveExtension();
        URL url = URL.valueOf("test://localhost/test?adaptive.ext=dog");
        adaptiveExtension.call(url);

    }

}
