package com.hlj.moudle.Jvm03类加载器;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * @Author HealerJean
 * @Date 2019/2/8  下午5:16.
 */
public class Jvm03ClassLoaderTest {

    public static void main(String[] args) throws Exception {

        //创建自定义classloader对象。
        Jvm03DiskClassLoader loader = new Jvm03DiskClassLoader("D:\\study\\HealerJean.github.io\\_posts\\8_Jvm\\hlj-jvm\\com-hlj-admin\\src\\main\\java");
        System.out.println(loader);
        //加载class文件
        Class c = loader.loadClass("com.hlj.moudle.Jvm03类加载器.Jvm02Test");
        System.out.println(loader.getParent());
        Object obj = c.newInstance();
        Method method = c.getDeclaredMethod("say", null);
        //通过反射调用Test类的say方法
        method.invoke(obj, null);

        //清除该类的实例
        obj = null;
        //清除该类的ClassLoader引用
        loader = null;
        //清除该class对象的引用
        c = null;
        // 执行一次gc垃圾回收
        System.gc();
    }

}
