package com.hlj.arith.demo00066_不同的二叉搜索树;

import org.junit.Test;

/**
作者：HealerJean
题目：不同的二叉搜索树1
 给定一个整数 n，求以 1 ... n 为节点组成的二叉搜索树有多少种？
解题思路：
 给定一个整数 n，求以 1 ... n 为节点组成的二叉搜索树有多少种？

 示例:
 输入: 3
 输出: 5
 解释:
 给定 n = 3, 一共有 5 种不同结构的二叉搜索树:
 1         3     3      2      1
 \       /     /      / \      \
 3     2     1      1   3      2
 /     /       \                 \
 2     1         2                 3
*/
public class 不同的二叉搜索树_1 {


    @Test
    public void test(){

        System.out.println(numTrees(3));
    }

    /**
     * 所求即为：G（n）
     */
    public int numTrees(int n) {
        int[] dp = new int[n+1];
        //空数最为第一个，而且下面要用，所以上面的数组个数为n+1
        dp[0] = 1;
        dp[1] = 1;

        // j 为有多少个数字要组成二叉搜索树
        //j = 0 和 j - 1 提前给出来了，所以j从2开始计算，
        for(int j = 2; j <= n; j++) {
            //i 为当前跟节点 ,该循环相当于就是将, j个数字都作为节点的时相加就是 dp[j]
            for(int i = 1; i <= j; i++) {
                dp[j] += dp[i-1] * dp[j-i];
            }
        }
        return dp[n];
    }

}
