package com.hlj.moudle.design.D00_单例模式;

import org.junit.jupiter.api.Test;

/**
 * @author HealerJean
 * @date 2020/12/3  10:35.
 * @description
 */
public class TestMain {

    @Test
    public void test(){
        System.out.println(SingtetonEnum.valueOf("EFFECT").getCode());
    }
}
