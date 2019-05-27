package com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D02_使用委托的适配器;


import com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D00_适配前.Show;

public abstract class Print implements Show {

    abstract void printWeak();
    abstract void printStrong();

}
