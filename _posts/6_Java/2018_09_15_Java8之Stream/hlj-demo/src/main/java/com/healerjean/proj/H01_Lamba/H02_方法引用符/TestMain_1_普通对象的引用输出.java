package com.healerjean.proj.H01_Lamba.H02_方法引用符;

import com.healerjean.proj.H01_Lamba.H02_方法引用符.inter.PrinterInterface;
import com.healerjean.proj.H01_Lamba.H02_方法引用符.dto.BeanObjectDTO;

import org.junit.Test;


public class TestMain_1_普通对象的引用输出 {

    @Test
    public void test() {

        //1、lambda 常规:表达式写法
        method(s -> {
            System.out.println(s.toUpperCase());
        });
        method(System.out::println);


        //2、构造器引用
        method((s) -> {
            new BeanObjectDTO(s);
        });
        method(BeanObjectDTO::new);


        //3、对象的引用输出
        method(s -> {
            new BeanObjectDTO().printStringUpper(s);
        });
        method(new BeanObjectDTO()::printStringUpper);


        //4、静态类方法引用
        method(s -> {
            BeanObjectDTO.staticPrintStringUpper(s);
        });
        method(BeanObjectDTO::staticPrintStringUpper);
    }


    public void method(PrinterInterface printerInterface) {
        printerInterface.print("HealerJean");
    }

}
