package com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式;

/**
 *  机器 ： 开启 运行 关闭
 */
public abstract class AbstractMachine {

    public abstract void open();

    public abstract void run();

    public abstract void close();
}
