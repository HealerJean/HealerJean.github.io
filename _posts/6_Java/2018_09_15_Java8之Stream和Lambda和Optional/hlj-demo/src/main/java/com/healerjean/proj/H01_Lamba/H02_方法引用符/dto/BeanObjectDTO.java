package com.healerjean.proj.H01_Lamba.H02_方法引用符.dto;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/14  下午4:15.
 */
public class BeanObjectDTO {

    public BeanObjectDTO() {
    }

    /**
     * 构造器引用
     */
    public BeanObjectDTO(String s) {
        System.out.println("构造器引用"+s.toUpperCase());
    }

    /**
     * 静态类方法引用  MethodRefObject::staticPrintStringUpper
     */
    public static void staticPrintStringUpper(String string){
        System.out.println("静态类方法引用"+string.toUpperCase());
    }


    /**
     * 非静态的方法引用  new MethodRefObject()::printStringUpper
     */
    public void printStringUpper(String s) {
        System.out.println("非静态的方法引用"+s.toUpperCase());
    }

}
