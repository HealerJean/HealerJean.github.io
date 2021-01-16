package com.hlj.arith.demo00089_最长连续序列;


import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
作者：HealerJean
题目：
 给定一个未排序的整数数组，找出最长连续序列的长度。
 要求算法的时间复杂度为 O(n)。
 示例:
    输入: [100, 4, 200, 1, 3, 2]
     输出: 4
     解释: 最长连续序列是 [1, 2, 3, 4]。它的长度为 4。
`解题思路：
*/
public class 最长连续序列 {

    @Test
    public void test(){
        int[] nums = {100,4,200,3,2};
        System.out.println(longestConsecutive(nums));
    }


    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet();
        for (int num : nums) {
            set.add(num);
        }

        int maxLen = 0;

        for (int num : set) {
            if (!set.contains(num - 1)) {
                int curLen = 1;

                while (set.contains(num + 1)) {
                    curLen += 1;
                    num += 1;
                }
                maxLen = Math.max(maxLen, curLen);
            }
        }
        return maxLen;
    }

}
