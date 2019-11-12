package com.healerjean.proj.reflect.extend;

import lombok.Data;

/**
 * @author HealerJean
 * @ClassName Father
 * @date 2019/11/12  13:34.
 * @Description
 */
@Data
public class Father {

    public String name = "父亲";

    public String fatherVar;

    public String printName() {
        return this.name;
    }

}
