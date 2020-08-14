package com.healerjean.proj.H01_Lamba.H02_方法引用符;

import com.healerjean.proj.H01_Lamba.H02_方法引用符.inter.PrinterInterface;
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
        //  str 就是 我们接口的参数，
        //  System.out.println(str) => 接口的实现方法
        print(str -> System.out.println(str));
        print(System.out::println);
    }


    public void print(PrinterInterface printerInterface) {
        printerInterface.print("HealerJean");
    }

}
