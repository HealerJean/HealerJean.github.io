package com.healerjean.proj.H01_Lamba.H04_功能性.H02_Consumer;

import org.junit.Test;

import java.util.function.Consumer;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        method(str -> System.out.printf(str));
        method(str -> System.out.printf(str), str -> System.out.println(str));

    }
    public void method(Consumer<String> consumer) {
        consumer.accept("HealerJean");
    }
    public void method(Consumer<String> consumer1, Consumer<String> consumer2) {
        // 先执行1，再执行2
        consumer1.andThen(consumer2).accept("HealerJean");
    }

}
