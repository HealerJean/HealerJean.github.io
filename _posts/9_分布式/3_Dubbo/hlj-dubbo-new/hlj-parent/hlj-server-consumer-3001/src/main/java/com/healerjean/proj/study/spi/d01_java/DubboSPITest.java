package com.healerjean.proj.study.spi.d01_java;

import com.healerjean.proj.study.spi.d01_java.api.Robot;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

@Slf4j
public class DubboSPITest {

    @Test
    public void sayHello()  {
        ExtensionLoader<Robot> extensionLoader =  ExtensionLoader.getExtensionLoader(Robot.class);
        log.info("获取接口实现类的名称 {}", extensionLoader.getSupportedExtensions());
        log.info("----------------------------");

        Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
        optimusPrime.sayHello();

        Robot bumblebee = extensionLoader.getExtension("bumblebee");

        bumblebee.sayHello();
    }
}
