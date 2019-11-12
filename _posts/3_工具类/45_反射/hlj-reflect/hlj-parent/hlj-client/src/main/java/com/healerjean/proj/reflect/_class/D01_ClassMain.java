package com.healerjean.proj.reflect._class;

import com.healerjean.proj.reflect.ReflectDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName D01_ClassMain
 * @date 2019/11/12  11:17.
 * @Description
 */
@Slf4j
public class D01_ClassMain {

    /**
     * 获取Class
     */
    @Test
    public void test1() throws ClassNotFoundException {

        Class reflectClass = Class.forName("com.healerjean.proj.reflect.ReflectDTO");
        log.info("获取Class方法一：【{}】", reflectClass.toGenericString());

        reflectClass = ReflectDTO.class;
        log.info("获取Class方法二：【{}】", reflectClass.toGenericString());

        reflectClass = new ReflectDTO().getClass();
        log.info("获取Class方法三：【{}】", reflectClass.toGenericString());

        // public class com.healerjean.proj.reflect.ReflectDTO
        // public class com.healerjean.proj.reflect.ReflectDTO
        // public class com.healerjean.proj.reflect.ReflectDTO
    }


}
