package com.hlj.arith.demo00153_最长上升子序列;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个无序的整数数组，找到其中最长上升子序列的长度。
 示例:
     输入: [10,9,2,5,3,7,101,18]
     输出: 4
     解释: 最长的上升子序列是 [2,3,7,101]，它的长度是 4。
解题思路：
*/
public class 最长上升子序列 {


    @Test
    public void test() {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        // int[] nums = {3,4,5,2};
        // int[] nums = {-2, -1};
        // int[] nums = {10, 9, 2, 5, 3, 4};
        // int[] nums = {};
        // int[] nums = {0};
        // int[] nums = {2,2};
        System.out.println(lengthOfLIS(nums));
    }

    /**
     * 2、动态规划
     */
    public int lengthOfLIS(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        // 定义 dp[i] 对应数组小标为i的时候的最长长度
        int[] dp = new int[nums.length];
        dp[0] = 1; //第一个肯定长度为1
        int maxLen = 1; //num数组有值，最小肯定是1
        for (int i = 1; i < dp.length; i++) {
            int temLen = 0; //初始为,后面最少也会加1
            //每轮求出最大长度
            for (int j = 0; j < i; j++) {
                //dp[j] 为当前0 到i 的时候的最长的子序列的长度。下面的比较其实就是
                if (nums[j] < nums[i]  ) {
                    temLen = Math.max(temLen, dp[j]);
                }
            }
            //比较完成之后，要记得加上当前的值哦
            dp[i] = temLen + 1;
            //获取最大长度
            maxLen = Math.max(maxLen, dp[i]);
        }
        return maxLen;
    }


    /** 1、超时 */
    public int lengthOfLIS2(int[] nums) {
        List<List<Integer>> subsequences = findSubsequences(nums);
        int max  = 0 ;
        for (List<Integer> list : subsequences){
            max = Math.max(list.size(), max);
        }
        return max;
    }


    public List<List<Integer>> findSubsequences(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList();
        dfs(0, Integer.MIN_VALUE, nums,res, linkedList);
        return res;
    }

    public void dfs(int index, int last, int[] nums, List<List<Integer>> res, LinkedList<Integer> linkedList) {
        //start 每次都会走到最后()
        if (index == nums.length) {
            res.add(new ArrayList<>(linkedList));
            return;
        }

        //如果当前的比上一个大或者等于的话，就进入，然后指针向后移动1位 start + 1
        if (nums[index] > last) {
            linkedList.add(nums[index]);
            dfs(index + 1, nums[index], nums, res, linkedList);
            linkedList.removeLast();
        }

        //此时回溯会结束，上一个元素已经被removeLast了，这个时候 相等的话就没有必要走了，因为这样会重复。如果不相等的话，则继续后面的遍历，相当于我们又从头开始一遍了
        if (nums[index] != last) {
            dfs(index + 1, last, nums, res, linkedList);
        }
    }
}
