package com.hlj.arith.demo00045_跳跃游戏;

import org.junit.Test;

/**
作者：HealerJean
题目：跳跃游戏_2 ,能否调到最后
 给定一个非负整数数组，你最初位于数组的第一个位置。
 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 判断你是否能够到达最后一个位置。
     示例 1:
         输入: [2,3,1,1,4]
         输出: true
         解释: 我们可以先跳 1 步，从位置 0 到达 位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。
     示例 2:
         输入: [3,2,1,0,4]
         输出: false
         解释: 无论怎样，你总会到达索引为 3 的位置。但该位置的最大跳跃长度是 0 ， 所以你永远不可能到达最后一个位置。
解题思路：
*/
public class 跳跃游戏_2 {

    @Test
    public void test(){
        int[] nums = {3,2,1,1,0};
        // int[] nums = {2,3,1,1,4};
        System.out.println(canJump(nums));
    }


    public boolean canJump(int[] nums) {
        int maxPositoin = 0 ;
        //如果能跳到，倒数第二位，并且满足下面的条件。就能跳跃成功
        for (int i = 0 ; i  < nums.length -1   ; i++ ){
            maxPositoin = Math.max(nums[i] + i , maxPositoin) ;
            //i 到达最终最大位移位置，并且 当前位置为0 ，表示不能跳跃，则结束
            if (i == maxPositoin && nums[i] == 0){
                return false;
            }
        }
        return true ;
    }
}



