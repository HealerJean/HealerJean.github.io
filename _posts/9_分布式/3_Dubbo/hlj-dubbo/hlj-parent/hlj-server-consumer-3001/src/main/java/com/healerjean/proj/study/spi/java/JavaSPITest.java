package com.healerjean.proj.study.spi.java;

import com.healerjean.proj.study.spi.Animal;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ServiceLoader;

/**
 * @author HealerJean
 * @ClassName JavaSPITest
 * @date 2020/6/24  18:43.
 * @Description
 */
@Slf4j
public class JavaSPITest {

    @Test
    public void sayHello() {
        ServiceLoader<Animal> serviceLoader = ServiceLoader.load(Animal.class);
        log.info("Java SPI--------");
        // serviceLoader.forEach(annoimal -> annoimal.call());
    }
}
