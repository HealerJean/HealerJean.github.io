package com.healerjean.proj.reflect.extend;

import lombok.Data;

/**
 * @author HealerJean
 * @ClassName Son
 * @date 2019/11/12  13:34.
 * @Description
 */
@Data
public class Son extends Father {

    public String name = "儿子";

    public String sonVar;

    @Override
    public String printName() {
        return this.name;
    }
}
