package com.healerjean.proj.study.spi.d01_java.api.impl;

import com.healerjean.proj.study.spi.d01_java.api.Robot;
import lombok.extern.slf4j.Slf4j;

/**
 * 擎天柱
 */
@Slf4j
public class OptimusPrime implements Robot {

    @Override
    public void sayHello() {
        log.info("Hello, I am Optimus Prime.");
    }
}
