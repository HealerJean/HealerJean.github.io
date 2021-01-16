package com.hlj.arith.demo00018_动态规划.Z01_爬楼梯;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName Z01_爬楼梯
 * @date 2020/1/19  11:40.
 * @Description
 */
public class Z01_3_爬楼梯_动态规划 {

    @Test
    public void method3() {
        System.out.println(f3(10));
    }
    public int f3(int n) {
        //第1级台阶有1中走法
        if (n == 1) {
            return 1;
        }
        //第2级台阶有两种走法
        if (n == 2) {
            return 2;
        }

        //从3级台阶开始，每次都等于前两级，相加，a是倒数第2级，b是倒数第1级
        int a = 1;
        int b = 2;
        int temp = 0;
        for (int i = 3; i <= n; i++) {
            temp = a + b;
            a = b;
            b = temp;
        }
        return temp;
    }

}
