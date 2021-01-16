package com.hlj.arith.demo00102_长度最小的子数组;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：长度最小的子数组
 给定一个含有 n 个正整数的数组和一个正整数 s ，找出该数组中满足其和 ≥ s 的长度最小的连续子数组，并返回其长度。如果不存在符合条件的连续子数组，返回 0。
 示例: 
     输入: s = 7, nums = [2,3,1,2,4,3]
     输出: 2
     解释: 子数组 [4,3] 是该条件下的长度最小的连续子数组。
解题思路：
*/
public class 长度最小的子数组 {

    @Test
    public void test() {
        // int s = 15;
        // int[] nums = {5,1,3,5,10,7,4,9,2,8};

        // int s = 4;
        // int[] nums = {1,4,4};

        int s = 7;
        int[] nums = {2,3,1,2,4,3 };
        System.out.println(minSubArrayLen(s, nums));
    }

    /**
     * 利用队列解决（也是相当于移动指针）(保证每次栈执行的时候数据是小于 s的)
     */
    public int minSubArrayLen1(int s, int[] nums) {
        Queue<Integer> queue = new LinkedList<>();
        int sum = 0 ;
        int len = Integer.MAX_VALUE ;
        for (int num:nums){
            queue.add(num);
            sum = sum + num;
            if (sum >= s){
                len = Math.min(queue.size(),  len);
                sum = sum - queue.remove();

                //此时sum就是队列的总数，因此只要sum大于s，则队列肯定不为空，所以无需判断队列是否为空的条件
                while (sum >= s){
                    len = Math.min(queue.size(),  len);
                    sum = sum - queue.remove();
                }
            }
        }
        //防止数组为空，或者不存在
        return len == Integer.MAX_VALUE ? 0 : len;
    }


    /**
     * 移动指针 (其实原理和上面的相同)
     */
    public int minSubArrayLen(int s, int[] nums) {
        int sum = 0 ;
        int len = Integer.MAX_VALUE ;
        int start = 0 ;
        int end = 0 ;
        for (int i = 0; i < nums.length; i++) {
            sum = sum + nums[i];
            end++ ;
            if (sum >= s){
                len = Math.min(len, end - start);
                sum = sum - nums[start];
                start++ ;
                while (sum >= s){
                    len = Math.min(len, end - start);
                    sum = sum - nums[start];
                    start ++ ;
                }
            }
        }
        //防止数组为空，或者不存在
        return len == Integer.MAX_VALUE ? 0 : len;
    }
}
