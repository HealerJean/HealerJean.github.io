package com.healerjean.proj.H01_Lamba.H04_功能性.H01_Function;

import org.junit.Test;

import java.util.function.Function;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  18:55.
 * @Description
 */
public class TestMain {

    @Test
    public void demo() {
        method(s -> Integer.parseInt(s));
        method(Integer::parseInt);
    }

    /**
     * Function<String, Integer> 前面是参数类型，后面是返回结果
     */
    public void method(Function<String, Integer> function) {
        int num = function.apply("20");
        num += 100;
        System.out.println("结果是" + num);
    }
}
