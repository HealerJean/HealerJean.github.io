package com.hlj.proj.business.function;

/**
 * @author zhangyujin
 * @date 2022/1/17  9:43 下午.
 * @description
 */
public interface IParseFunction {

    default boolean executeBefore(){
        return false;
    }

    String functionName();

    String apply(String value);
}
