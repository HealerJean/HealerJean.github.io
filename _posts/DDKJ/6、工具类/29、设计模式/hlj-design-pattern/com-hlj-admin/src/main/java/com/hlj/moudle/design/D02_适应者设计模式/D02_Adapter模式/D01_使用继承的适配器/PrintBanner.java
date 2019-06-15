package com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D01_使用继承的适配器;


import com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D00_适配前.Banner;

/**
 *  适配器
 */
public class PrintBanner extends Banner implements Print {

    public PrintBanner(String string) {
        super(string);
    }

    public void printWeak() {
        System.out.println("(" + string );
    }

    public void printStrong() {
        System.out.println("*" + string );
    }
}
