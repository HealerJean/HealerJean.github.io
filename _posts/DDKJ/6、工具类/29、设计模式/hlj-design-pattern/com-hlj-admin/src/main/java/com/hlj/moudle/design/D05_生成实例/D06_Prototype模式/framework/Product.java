package com.hlj.moudle.design.D05_生成实例.D06_Prototype模式.framework;

import java.lang.Cloneable;

public interface Product extends Cloneable {

    void use(String s);

    Product createClone();
}
