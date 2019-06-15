package com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D01_使用继承的适配器;

import com.hlj.moudle.design.D02_适应者设计模式.D02_Adapter模式.D00_适配前.Show;

public interface Print  extends Show {

    void printWeak();

    void printStrong();

}
