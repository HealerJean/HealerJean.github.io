package com.hlj.arith.demo00060_买卖股票的最佳时机;


import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个整数数组，其中第 i 个元素代表了第 i 天的股票价格 。​
 设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:
 你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。
 卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。
     示例:
         输入: [1,2,3,0,2]
         输出: 3
         解释: 对应的交易状态为: [买入, 卖出, 冷冻期, 买入, 卖出]
解题思路：


*/
public class 最佳买卖股票时机含冷冻期 {

    @Test
    public void test() {
        int[] prices = {1, 2, 3, 0, 2};
        System.out.println(maxProfit(prices));
    }

    public int maxProfit(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }

        int n = prices.length;
        // f[i][0]: 手上持有股票的最大收益
        // f[i][1]: 手上不持有股票，并且处于冷冻期中的累计最大收益
        // f[i][2]: 手上不持有股票，并且不在冷冻期中的累计最大收益
        int[][] dp = new int[n][3];

        //第一天只能买入股票
        dp[0][0] = -prices[0];
        for (int i = 1; i < n; ++i) {
            // Math.max （上一天第一次买入股票手里还剩多少钱 ，上一天不在冷冻区并且又在当天买入股票后手里还剩多少钱）
            dp[i][0] = Math.max(dp[i - 1][0] , dp[i - 1][2] - prices[i] );

            //准备进入冷冻区，上一天买入股票后，今天卖出股票后开始进入冷冻期
            dp[i][1] = dp[i - 1][0] + prices[i];

            // 不在冷冻期手里剩多少钱   Math.max （上一天不在冷冻期手里剩多少钱，上一天在冷冻期（那么今天就不是冷冻期）手里还剩多少钱）
            dp[i][2] = Math.max(dp[i - 1][2], dp[i - 1][1]);
        }

        //手上不持有股票的时候，手里剩多少钱
        return Math.max(dp[n - 1][1], dp[n - 1][2]);
    }


}
