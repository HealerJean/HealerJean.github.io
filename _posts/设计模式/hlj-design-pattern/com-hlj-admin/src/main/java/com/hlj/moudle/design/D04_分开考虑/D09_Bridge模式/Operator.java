package com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式;

/**
 * 展示机器类
 */
public class Operator {

    private AbstractMachine machine;

    /**
     * 使用哪一台机器
     */
    public Operator(AbstractMachine impl) {
        this.machine = impl;
    }

    public void open() {
        machine.open();
    }

    public void run() {
        machine.run();
    }

    public void close() {
        machine.close();
    }

    public  void task() {
        open();
        run();
        close();
    }
}
