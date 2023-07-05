package com.healerjean.proj.reflect;

import lombok.Data;

/**
 * @author zhangyujin
 * @date 2023/7/3$  09:31$
 */
@Data
public class MethodPerson {
    private String name;
    private String type;
    private int camp;

    public MethodPerson() {
    }

    public MethodPerson(String name, String type, int camp) {
        super();
        this.name = name;
        this.type = type;
        this.camp = camp;
    }

    @Override
    public String toString() {
        return "MethodPerson [\n name=" + name + ", \n type=" + type + ", \n camp=" + camp + "\n]";
    }

}
