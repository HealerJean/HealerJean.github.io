package com.healerjean.proj.a_test.json.jackson.d03_选择性过滤;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author HealerJean
 * @date 2020/9/16  14:45.
 * @description
 */

public interface PropertyFunction<T, R> extends Function<T, R>, Serializable {

}


