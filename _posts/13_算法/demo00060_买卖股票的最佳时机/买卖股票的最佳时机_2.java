package com.hlj.arith.demo00060_买卖股票的最佳时机;

import org.junit.Test;

/**
作者：HealerJean
题目：买卖股票的最佳时机_2
 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
 注意：你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。

 示例 1:
     输入: [7,1,5,3,6,4]
     输出: 7
     解释: 在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。
          随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 这笔交易所能获得利润 = 6-3 = 3 。
 示例 2:
     输入: [1,2,3,4,5]
     输出: 4
     解释: 在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。
          注意你不能在第 1 天和第 2 天接连购买股票，之后再将它们卖出。
          因为这样属于同时参与了多笔交易，你必须在再次购买前出售掉之前的股票。
 示例 3:
     输入: [7,6,4,3,1]
     输出: 0
     解释: 在这种情况下, 没有交易完成, 所以最大利润为 0。
    解题思路：这道题的思想和 多数元素、
*/
public class 买卖股票的最佳时机_2 {

    @Test
    public void test(){
        int[] prices = {1,2,3,4,5} ;
        System.out.println(maxProfit(prices));
    }

    public int maxProfit(int prices[]) {
        if (prices.length == 0){
            return 0;
        }

        //flag 可以重新买入：true，可以随时买入卖出：false
        boolean flag = false ;
        int min = prices[0]; //买入的最低价格
        int res = 0;
        for (int i = 1; i < prices.length; i++) {
            //已经卖出了， 此时可以买入了，我们要设置当前最低价格
            if (flag){
                min = prices[i];
                flag = false;
             //可以卖出
            }else {
                //如果当前价格比买入的价格还要低或者相等的时候，设置最低买入价格，i继续向前移动
                //我们是要买入，相等的时候，你也不会卖出呀，所以一直到prices[i] 直最后的结果肯定比min大，这样才会
                if (prices[i] <= min) {
                    min = prices[i];
                }else {
                    //min肯定会有值，如果可以买入，
                    // 并且买入价格是按照比第二条的价格高或者相等的时候会截至，因为你想相等了还可以买入呀，
                    // 的时候或者是数组最后一个元素的时候，要卖出，否则继续移动i指针
                    if ((i != prices.length-1) && (prices[i] > prices[i+1]) || i == prices.length-1){
                        res = res + prices[i] - min;
                        flag = true;
                    }
                }
            }
        }
        return res;
    }


}
