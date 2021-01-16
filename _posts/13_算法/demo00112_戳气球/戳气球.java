package com.hlj.arith.demo00112_戳气球;


import org.junit.Test;

/**
作者：HealerJean
题目：
 有 n 个气球，编号为0 到 n-1，每个气球上都标有一个数字，这些数字存在数组 nums 中。
 现在要求你戳破所有的气球。如果你戳破气球 i ，就可以获得 nums[left] * nums[i] * nums[right] 个硬币。 这里的 left 和 right 代表和 i 相邻的两个气球的序号。注意当你戳破了气球 i 后，气球 left 和气球 right 就变成了相邻的气球。
 求所能获得硬币的最大数量。
 说明:
     你可以假设 nums[-1] = nums[n] = 1，但注意它们不是真实存在的所以并不能被戳破。
     0 ≤ n ≤ 500, 0 ≤ nums[i] ≤ 100
 示例:
     输入: [3,1,5,8]
     输出: 167
     解释: nums = [3,1,5,8] --> [3,5,8] -->   [3,8]   -->  [8]  --> []
          coins =  3*1*5      +  3*5*8    +  1*3*8      + 1*8*1   = 167
解题思路：
*/
public class 戳气球 {


    @Test
    public void test() {
        int[] nums = {3, 1, 5, 8};
        System.out.println(maxCoins(nums));
    }

    public int maxCoins(int[] nums) {
        int n = nums.length;
        int[] val = new int[n + 2];
        val[0] = 1;
        val[n + 1] = 1;
        //讲 num数组整个 赋值到  val 中
        for (int i = 1; i <= n; i++) {
            val[i] = nums[i - 1];
        }

        //这里的i和j不是值数组下标，而是指第多少个气球
        // dp[i][j] : 表示nums中i到j的气球全部被戳破的集合，先戳破[i, k - 1]和[k + 1, j]的气球，最后戳破第k个气球获得转移方程。
        int[][] dp = new int[n + 2][n + 2];


        // 循环区间长度
        for (int len = 1; len <= n; len++) {
            // 循环遍历数组  从i开始 长度为len 必须满足 len <=  n - i + 1
            for (int i = 1; len <= n - i + 1; i++) {
                //如果长度为len 则 j的位置应该是 i + len - 1 (len是包含 i的哦)
                int j = i + len - 1;
                for (int k = i; k <= j; k++) {
                    //因为 k+
                    // dp[i][k - 1] + dp[k + 1][j] 表示 [i,k - 1] 与 [k + 1, j] 被戳破所获得的最大coins数，
                    //  val[i - 1] * val[k] * val[j + 1] 表示最后戳破第k个气球所获得coins数目。
                    dp[i][j] = Math.max(dp[i][j], dp[i][k - 1] + dp[k + 1][j] + val[i - 1] * val[k] * val[j + 1]);
                }
            }
        }
        return dp[1][n];
    }
}
