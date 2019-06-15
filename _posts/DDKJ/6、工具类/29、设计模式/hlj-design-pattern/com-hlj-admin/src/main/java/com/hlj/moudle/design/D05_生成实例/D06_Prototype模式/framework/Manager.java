package com.hlj.moudle.design.D05_生成实例.D06_Prototype模式.framework;

import java.util.*;

public class Manager {

    private HashMap showcase = new HashMap();

    public void register(String name, Product proto) {
        showcase.put(name, proto);
    }

    public Product create(String protoname) {
        Product p = (Product) showcase.get(protoname);
        return p.createClone();
    }
}
