package com.healerjean.proj.study.spi;

import org.junit.Test;

import java.util.ServiceLoader;

/**
 * @author HealerJean
 * @ClassName JavaSPITest
 * @date 2020/6/24  18:43.
 * @Description
 */
public class JavaSPITest {

    @Test
    public void sayHello() throws Exception {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        System.out.println("Java SPI");
        serviceLoader.forEach(Robot::sayHello);
    }
}
