package com.hlj.proj.utils.check;

import java.io.Serializable;
import java.util.function.Function;

/**
 * LambdaFunction
 *
 * @author zhangyujin
 * @date 2024/9/30
 */
public interface LambdaFunction<T, R> extends Function<T, R>, Serializable {

}