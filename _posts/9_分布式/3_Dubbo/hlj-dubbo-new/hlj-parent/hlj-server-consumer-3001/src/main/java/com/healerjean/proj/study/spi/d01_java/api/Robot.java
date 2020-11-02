package com.healerjean.proj.study.spi.d01_java.api;


import org.apache.dubbo.common.extension.SPI;

/**
 * 机器人
 */
@SPI
public interface Robot {

    void sayHello();
}


