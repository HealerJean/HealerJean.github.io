package com.hlj.arith.demo00119_剪绳子取模;


import org.junit.Test;


/**
作者：HealerJean
题目：
 给你一根长度为 n 的绳子，请把绳子剪成整数长度的 m 段（m、n都是整数，n>1并且m>1），每段绳子的长度记为 k[0],k[1]...k[m-1] 。请问 k[0]*k[1]*...*k[m-1] 可能的最大乘积是多少？例如，当绳子的长度是8时，我们把它剪成长度分别为2、3、3的三段，此时得到的最大乘积是18。
 提示：2 <= n <= 58
 示例 1：
         输入: 2
         输出: 1
         解释: 2 = 1 + 1, 1 × 1 = 1
     示例 2:
         输入: 10
         输出: 36
         解释: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36
解题思路：
*/
public class 剪绳子_取模 {


    @Test
    public void test() {
        System.out.println(cuttingRope(57));
    }


    public int cuttingRope(int n) {
        if (n <= 3) {
            return n - 1;
        }
        //防止一下子就超过Integer的最大值。使用long暂存一下
        long res = 1;
        while (n > 4) {
            res *= 3;
            n -= 3;
            res = res % 1000000007;
        }

        //上面 n 一直减去3，肯定会有剩余部分，剩余部分就是n，所以res要继续乘下去
        return (int) (res * n % 1000000007);
    }


    /** 动态规划，可能会导致一些未知的数据出现（因为结果是乘积试出来的），比如 有数据明明超过了  1000000007 ，但是提前取模之后的结果可能没有 1000000007 大*/
    // public int cuttingRope2(int n) {
    //     int[] dp = new int[n + 1];
    //     dp[1] = 1;
    //     for (int i = 2; i <= n; i++) {
    //         //从1开始减绳子，不会超过 i
    //         for (int j = 1; j < i; j++) {
    //             //如果只减1次
    //             dp[i] = Math.max(dp[i], j * (i - j));
    //             //如果减多次
    //             dp[i] = Math.max(dp[i], dp[i - j] * j);
    //
    //         }
    //     }
    //     return dp[n];
    // }

}
