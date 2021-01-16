package com.hlj.arith.demo00157_Nim游戏;

import org.junit.Test;

/**
作者：HealerJean
题目：
 你和你的朋友，两个人一起玩 Nim 游戏：桌子上有一堆石头，每次你们轮流拿掉 1 - 3 块石头。 拿掉最后一块石头的人就是获胜者。你作为先手。
 你们是聪明人，每一步都是最优解。 编写一个函数，来判断你是否可以在给定石头数量的情况下赢得游戏。
     示例:
         输入: 4
         输出: false
         解释: 如果堆中有 4 块石头，那么你永远不会赢得比赛；
              因为无论你拿走 1 块、2 块 还是 3 块石头，最后一块石头总是会被你的朋友拿走。
解题思路：
*/
public class Nim游戏 {


    @Test
    public void test(){
        for (int i = 0; i < 300; i++) {
            System.out.println(canWinNim(20) == canWinNim2(20));
        }
    }


    /**
     * 解法1，动态规划，思路正确就是超出内存限制了
     */
    public boolean canWinNim2(int n) {
        if (n == 1 || n == 2 || n == 3){
            return true;
        }
        boolean[] dp = new boolean[n + 1];
        dp[1] = true;
        dp[2] = true;
        dp[3] = true;
        for (int i = 4; i <= n; i++) {
            boolean flag = false;
            for (int j = 1; j <= 3; j++) {
                //只要有一个为false， 就表示有戏
                if (!dp[i-j]){
                    flag = false;
                    break;
                }
            }
            dp[i] =flag;
        }
        return dp[n];
    }


    /**
     * 解法2，官方解法（有些像数三十，避免自己数到27）
     * 避免自己最后就剩4 。所以不能选择4,只能选择1 2 3 , 5 6 7 ，看规律的话很明显，如果是4的倍数则false
     */
    public boolean canWinNim(int n) {
        if (n % 4 == 0) {
            return false;
        }
        return true;
    }
}
