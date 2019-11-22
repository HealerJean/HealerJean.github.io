package com.healerjean.proj.reflect._class;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName ClassMain
 * @date 2019/11/12  19:25.
 * @Description
 */
public class ClassMain {

    // 静态的参数初始化  //只会执行一次
    static {
        System.out.println("--静态的参数初始化--");
    }

    // 非静态的参数初始化   //动态 new 或者 newInstance 实例化对象的时候执行，可执行多次
    {
        System.out.println("--非静态的参数初始化--");
    }

    public ClassMain() {
        System.out.println("ClassTest!");
    }

    /**
     * 1、测试Class.forName
     **/
    // public static void main(String[] args) throws ClassNotFoundException {
    //     //类已加载并且这个类已连接，这是正是class的静态方法forName（）完成的工作。
    //     Class clazz = Class.forName("com.healerjean.proj.reflect._class.ClassMain");
    //     System.out.println(clazz);
    //
    //     // --静态的参数初始化--
    //     // class com.healerjean.proj.reflect._class.ClassMain
    // }


    /**
     * 2、比较 newInstance 和直接new一个对象
     * 2.1、使用newInstance可以解耦。使用newInstance的前提是，类已加载并且这个类已连接，
     * 这是正是class的静态方法forName（）完成的工作。newInstance实际上是把new 这个方式分解为两步，
     * 即，首先调用class的加载方法加载某个类，然后实例化
     * 2.2、newInstance: 弱类型。低效率。只能调用无参构造。 new Object(): 强类型。相对高效。能调用任何public构造。
     */
    // public static void main(String[] args) throws Exception {
    //     // 下面二者的是一样的结果
    //
    //
    //     // Class.forName("com.healerjean.proj.reflect._class.ClassMain").newInstance();
    //     // --静态的参数初始化--
    //     // --非静态的参数初始化--
    //     // ClassTest!
    //
    //     new ClassMain();
    //     // --静态的参数初始化--
    //     // --非静态的参数初始化--
    //     // ClassTest!
    //
    // }


    /***
     * 非静态的参数初始化 ,//动态 new 或者 newInstance 实例化对象的时候执行，可执行多次
     */
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("com.healerjean.proj.reflect._class.ClassMain").newInstance();
        new ClassMain();

        // --静态的参数初始化--
        //  --非静态的参数初始化--
        // ClassTest!
        //  --非静态的参数初始化--
        // ClassTest!
    }
}
