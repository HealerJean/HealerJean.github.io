package com.healerjean.proj.a_test.json.jackson.d03_选择性过滤;

import java.io.Serializable;
import java.util.function.Function;

/**
 * SFunction
 *
 * @author zhangyujin
 * @date 2024/9/30
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {

}
