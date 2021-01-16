package com.hlj.arith.demo00045_跳跃游戏;

import org.junit.Test;

/**
作者：HealerJean
题目：跳跃游戏_1 ,最少的步揍
 给定一个非负整数数组，你最初位于数组的第一个位置。数组中的每个元素代表你在该位置可以跳跃的最大长度。你的目标是使用最少的跳跃次数到达数组的最后一个位置。
解题思路：
*/
public class 跳跃游戏_1 {

    @Test
    public void test(){
        int[] nums = {2,3,1,1,4,2,1};
        System.out.println(jump(nums));
    }


    public int jump(int[] nums) {
        int end = 0;
        int maxPosition = 0;
        int steps = 0;
        for(int i = 0; i < nums.length - 1; i++){
            //获取当前最远边界的位置
            maxPosition = Math.max(maxPosition, nums[i] + i);

            //更新边界
            //上面是我们尽可能在走到边界的时候，获取跳跃最远的位置，目的就是我们当我们走到边界的时候，设置下一个边界的位置
            if( i == end){
                end = maxPosition;
                steps++;
            }
        }
        return steps;
    }
}



