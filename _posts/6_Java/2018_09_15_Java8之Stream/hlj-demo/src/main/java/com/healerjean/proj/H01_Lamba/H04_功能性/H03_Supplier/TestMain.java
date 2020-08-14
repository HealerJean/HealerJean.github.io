package com.healerjean.proj.H01_Lamba.H04_功能性.H03_Supplier;

import org.junit.Test;

import java.util.function.Supplier;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        method(() -> "HealerJean");
    }
    public void method(Supplier<String> supplier){
        String str = supplier.get();
        System.out.println(str);
    }
}
