package com.healerjean.proj.a_test;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName D04_PathMain
 * @date 2019/12/13  10:50.
 * @Description
 */
public class D04_PathMain {

    @Test
    public void testClassPath() {
        // System.out.println(this.getClass().getResource(""));
        System.out.println(this.getClass().getClassLoader().getResource("/1"));
    }

}
