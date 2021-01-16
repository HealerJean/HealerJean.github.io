package com.hlj.arith.demo00121_斐波那契数列取模;

import org.junit.Test;

/**
作者：HealerJean
题目：
 写一个函数，输入 n ，求斐波那契（Fibonacci）数列的第 n 项。斐波那契数列的定义如下：
 F(0) = 0,   F(1) = 1
 F(N) = F(N - 1) + F(N - 2), 其中 N > 1.
 斐波那契数列由 0 和 1 开始，之后的斐波那契数就是由之前的两数相加而得出。
 答案需要取模 1e9+7（1000000007），如计算初始结果为：1000000008，请返回 1。
     示例 1：
         输入：n = 2
         输出：1
     示例 2：
         输入：n = 5
         输出：5
解题思路：
*/
public class 斐波那契数列取模 {

    @Test
    public void test(){
        System.out.println(fib(5));
    }


    /** 正向 */
    public int fib(int n) {

        int pre = 0;
        int post = 1;
        int res = 0;
        for (int i = 2; i <= n; i++) {
            //答案要取模
            res = (pre + post) % 1000000007;

            pre = post;
            post = res;
        }
        return n == 0 ? 0 : n == 1 ? 1 : res ;
    }

}
