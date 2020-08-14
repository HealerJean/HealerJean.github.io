package com.healerjean.proj.H01_Lamba.H04_功能性.H04_Predicate;

import org.junit.Test;

import java.util.function.Predicate;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        method(str ->  str.equals("HealerJean"));

        method(str ->  str.equals("HealerJean"), str ->  str.equals("Jean"));

    }

    public void method(Predicate<String> predicate){
        boolean flag = predicate.test("HealerJean");
        //取反
        boolean flag2 = predicate.negate().test("HealerJean");

    }
    public void method(Predicate<String> predicate1, Predicate<String> predicate2){
        // and两个同时成立 ，or有一个成立即为true
        boolean flag = predicate1.and(predicate2).test("HealerJean");
        boolean flag2 = predicate1.or(predicate2).test("HealerJean");
    }
}
