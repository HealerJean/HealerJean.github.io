package com.hlj.arith.demo00082_打家劫舍;

import org.junit.Test;

/**
作者：HealerJean
题目：
 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 给定一个代表每个房屋存放金额的非负整数数组，计算你 不触动警报装置的情况下 ，一夜之内能够偷窃到的最高金额。
 示例 1:
     输入: [1,2,3,1]
     输出: 4
     解释: 偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
          偷窃到的最高金额 = 1 + 3 = 4 。
 示例 2:
     输入: [2,7,9,3,1]
     输出: 12
     解释: 偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
          偷窃到的最高金额 = 2 + 9 + 1 = 12 。
解题思路：
 1、如果只有一间房屋，则偷窃该房屋，可以偷窃到最高总金额。如果只有两间房屋，则由于两间房屋相邻，不能同时偷窃，只能偷窃其中的一间房屋，因此选择其中金额较高的房屋进行偷窃，可以偷窃到最高总金额。
 2、偷窃第 k 间房屋，那么就不能偷窃第 k−1 间房屋，偷窃总金额为前 k-2k−2 间房屋的最高总金额与第 kk 间房屋的金额之和。
 3、不偷窃第 k 间房屋，偷窃总金额为前 k−1 间房屋的最高总金额。
*/
public class 打家劫舍 {


    @Test
    public void test(){
        // int[] nums = {2,7,9,3,1} ;
        int[] nums = {1,2,3,1} ;
        System.out.println(rob(nums));
    }

    public int rob(int[] nums) {
        if (nums.length == 0){
            return 0;
        }
        if (nums.length == 1){
            return nums[0];
        }
       int[] dp = new int[nums.length] ;
        dp[0] = nums[0] ;
        dp[1] = Math.max(nums[0], nums[1]);
        for (int i = 2 ; i < nums.length; i ++){
            dp[i] = Math.max(dp[i-2] + nums[i], dp[i-1] );
        }
        return dp[nums.length-1] ;
    }

}
