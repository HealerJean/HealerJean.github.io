package com.healerjean.proj.study.spi.d01_java;

import com.healerjean.proj.study.spi.d01_java.api.Robot;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ServiceLoader;


/**
 * java spi 测试
 */
@Slf4j
public class JavaSPITest {

    @Test
    public void sayHello() {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        log.info("Java SPI--------");
        serviceLoader.forEach(robot -> robot.sayHello());
    }
}
