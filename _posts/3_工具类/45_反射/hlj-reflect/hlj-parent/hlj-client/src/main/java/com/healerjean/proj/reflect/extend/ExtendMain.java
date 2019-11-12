package com.healerjean.proj.reflect.extend;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName ExtendMain
 * @date 2019/11/12  13:35.
 * @Description
 */
@Slf4j
public class ExtendMain {


    /**
     * 如果在子类中声明了和父类名称一样的变量，则子类中对自己声明的变量的修改，不影响父类中改变量的值
     * 方法如果是子类的或者是子类重写的父类，则一定是调用的子类。
     * 变量直接赋值（son.name   sonFromfather.name），则一定是改变的自己的变量值或者是父类的变量值
     */
    @Test
    public void test1() {

        Father sonFromfather = new Son();
        log.info("sonFromfather.name：【{}】", sonFromfather.name);
        // log: 父亲 （这个变量是父类的）
        log.info("sonFromfather.printName：【{}】", sonFromfather.printName());
        // log: 儿子 (方法是子类的)

        // 下面修父类的变量
        sonFromfather.name = "sonFromfather------name";
        log.info("sonFromfather.name：【{}】", sonFromfather.name);
        // log: sonFromfather------name
        log.info("sonFromfather.name：【{}】", ((Son) sonFromfather).name);
        // log: 儿子 （打印的是子类的变量的值）
        log.info("sonFromfather.getName：【{}】", sonFromfather.getName());
        // log: 儿子（方法是子类的）


        Son son = new Son();
        son.name = "test";
        log.info("son.name：【{}】", son.getName());
        // log：test
        log.info("son.printName：【{}】", son.printName());
        // log：test


    }
}
