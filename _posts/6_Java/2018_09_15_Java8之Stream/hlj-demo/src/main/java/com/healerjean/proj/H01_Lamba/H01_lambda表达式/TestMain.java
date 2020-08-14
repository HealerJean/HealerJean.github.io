package com.healerjean.proj.H01_Lamba.H01_lambda表达式;

import com.healerjean.proj.H01_Lamba.H01_lambda表达式.inter.CalculatorInterface;
import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        sum((int a, int b) -> {
            return b + a;
        });

        sum((a, b) -> a + b);
    }

    public void sum(CalculatorInterface calculatorInterface) {
        int result = calculatorInterface.add(100, 200);
        System.out.println("结果是" + result);
    }

}
