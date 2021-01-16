package com.hlj.arith.demo00142_递增子序列;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个整型数组, 你的任务是找到所有该数组的递增子序列，递增子序列的长度至少是2。
     示例:
         输入: [4, 6, 7, 7]
         输出: [[4, 6], [4, 7], [4, 6, 7], [4, 6, 7, 7], [6, 7], [6, 7, 7], [7,7], [4,7,7]]
     说明:
         给定数组的长度不会超过15。
         数组中的整数范围是 [-100,100]。
         给定数组中可能包含重复数字，相等的数字应该被视为递增的一种情况。
解题思路：
*/
public class 递增子序列 {



    @Test
    public void test(){
        int[] nums = {4, 6, 7, 7};
        System.out.println(findSubsequences(nums));
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
            if (linkedList.size() > 1 ) {
                res.add(new ArrayList<>(linkedList));
            }
            return;
        }

        //如果当前的比上一个大或者等于的话，就进入，然后指针向后移动1位 start + 1
        if (nums[index] >= last) {
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
