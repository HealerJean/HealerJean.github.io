package com.hlj.moudle.design.D04_分开考虑.D09_Bridge模式;

/**
 * 定义机器： StringMachine机器
 */
public class StringMachine extends AbstractMachine {

    public String string;

    public StringMachine(String string) {
        this.string = string;
    }

    @Override
    public void open() {
        System.out.println(string+"开启");
    }

    @Override
    public void run() {
        System.out.println(string+"运行");
    }

    @Override
    public void close() {
        System.out.println(string+"关闭");
    }

}
