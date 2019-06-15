package com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D02_使用委托的适配器;


import com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D00_适配前.Banner;
import com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D00_适配前.Show;

/**
 *  适配器
 */
public class PrintBanner extends  Print {

    private Show show ;
    private String string ;

    public PrintBanner(String string) {
        this.string = string ;
        show = new Banner(string);
    }

    @Override
    public void showWithParen() {
        show.showWithParen();
    }

    @Override
    public void showWithAster() {
        show.showWithAster();
    }


    public void printWeak() {
        System.out.println("(" + string );
    }

    public void printStrong() {
        System.out.println("*" + string );
    }
}
