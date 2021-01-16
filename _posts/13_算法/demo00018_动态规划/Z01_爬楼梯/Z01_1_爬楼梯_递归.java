package com.hlj.arith.demo00018_动态规划.Z01_爬楼梯;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName Z01_爬楼梯
 * @date 2020/1/19  11:40.
 * @Description
 */
public class Z01_1_爬楼梯_递归 {

    @Test
    public void method() {
        System.out.println(f(10));
    }

    public int f(int n) {
        //第1级台阶有1中走法
        if (n == 1) {
            return 1;
        }

        //第2级台阶有两种走法
        if (n == 2) {
            return 2;
        }

        //其他台阶的走法是 如下
        return f(n - 1) + f(n - 2);
    }


}
