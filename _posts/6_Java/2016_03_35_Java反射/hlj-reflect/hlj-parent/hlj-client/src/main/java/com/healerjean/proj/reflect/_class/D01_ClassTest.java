package com.healerjean.proj.reflect._class;

import com.healerjean.proj.reflect.ReflectDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName D01_ClassTest
 * @date 2019/11/12  11:17.
 * @Description
 */
@Slf4j
public class D01_ClassTest {

    /**
     * 获取Class
     */
    @Test
    public void test1() throws ClassNotFoundException {

        Class reflectClass = Class.forName("com.healerjean.proj.reflect.ReflectDTO");

        reflectClass = ReflectDTO.class;

        reflectClass = new ReflectDTO().getClass();
    }


}
