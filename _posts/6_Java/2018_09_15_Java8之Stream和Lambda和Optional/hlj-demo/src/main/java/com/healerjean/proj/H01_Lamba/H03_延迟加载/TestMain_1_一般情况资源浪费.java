package com.healerjean.proj.H01_Lamba.H03_延迟加载;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain_1_一般情况资源浪费 {

    @Test
    public void test1() {
        String msgA = "Hello";
        String msgB = "你好";
         // 如果level等于2的时候， 使用这种方法就白白拼接了，因为最终结果中也没有打印
        logger(2, msgA + msgB);
    }
    /**
     * 日志级别为1的时候，打印信息
     */
    private void logger(int level, String msg) {
        if (level == 1) {
            System.out.println(msg);
        }
    }
}
