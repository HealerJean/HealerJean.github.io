package com.hlj.arith.demo00117_除数博弈;

import org.junit.Test;

/**
作者：HealerJean
题目：
 爱丽丝和鲍勃一起玩游戏，他们轮流行动。爱丽丝先手开局。
 最初，黑板上有一个数字 N 。在每个玩家的回合，玩家需要执行以下操作：
 选出任一 x，满足 0 < x < N 且 N % x == 0 。
 用 N - x 替换黑板上的数字 N 。
 如果玩家无法执行这些操作，就会输掉游戏。
 只有在爱丽丝在游戏中取得胜利时才返回 True，否则返回 false。假设两个玩家都以最佳状态参与游戏。
 示例 1：
     输入：2
     输出：true
     解释：爱丽丝选择 1，鲍勃无法进行操作。
 示例 2：
     输入：3
     输出：false
     解释：爱丽丝选择 1，鲍勃也选择 1，然后爱丽丝无法进行操作。
解题思路：
*/
public class 除数博弈 {

    @Test
    public void test(){
        System.out.println(divisorGame(10));
    }

    /** 爱丽丝为 偶数肯定能赢 */
    public boolean divisorGame2(int N) {
        return N % 2 == 0;
    }


    public boolean divisorGame(int N) {

        if (N ==1){
            return false ;
        }

        boolean[] dp = new boolean[N + 1];

        dp[1] = false;
        dp[2] = true;
        for (int i = 3; i <= N; ++i) {
            for (int j = 1; j < i; ++j) {
                // f[i-j] 为后面的人肯定会输false 时，当前人会赢
                if ((i % j) == 0 && !dp[i - j]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[N];
    }



}
