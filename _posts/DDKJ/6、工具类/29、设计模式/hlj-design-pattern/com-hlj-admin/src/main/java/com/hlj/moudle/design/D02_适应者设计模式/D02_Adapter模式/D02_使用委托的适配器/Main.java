package com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D02_使用委托的适配器;


public class Main {
    public static void main(String[] args) {

        //适配后
        Print p = new PrintBanner("Hello");
        p.printWeak();
        p.printStrong();

        p.showWithAster();
        p.showWithParen();
    }


}
