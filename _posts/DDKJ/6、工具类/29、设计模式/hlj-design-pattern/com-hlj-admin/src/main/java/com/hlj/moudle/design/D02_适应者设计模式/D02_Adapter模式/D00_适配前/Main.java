package com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D00_适配前;

import com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D01_使用继承的适配器.PrintBanner;

public class Main {
    public static void main(String[] args) {
        //适配前
        Show  s = new PrintBanner("\"Hello\"") ;
        s.showWithAster();
        s.showWithParen();
    }


}
