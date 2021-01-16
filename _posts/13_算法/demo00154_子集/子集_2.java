package com.hlj.arith.demo00154_子集;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个可能包含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
 说明：解集不能包含重复的子集。(有顺序)
    示例:
     输入: [1,2,2]
     输出:
         [
         [2],
         [1],
         [1,2,2],
         [2,2],
         [1,2],
         []
         ]
解题思路：
*/
public class 子集_2 {


    @Test
    public void test(){
        // int[] nums = {1,2,2};
        int[] nums = {4,4,4,1,4};
        System.out.println(subsetsWithDup(nums));
    }

    public List<List<Integer>> subsetsWithDup(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        boolean[] used = new boolean[nums.length];
        dsf(0, nums, res , linkedList, used);
        return res;
    }

    public void dsf(int index, int[]  nums,List<List<Integer>> res, LinkedList<Integer> linkedList,  boolean[] used){
        res.add(new ArrayList<>(linkedList));
        for (int i = index; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i-1] && !used[i-1]){
                continue;
            }
            linkedList.add(nums[i]);
            used[i] = true;
            dsf(i+1, nums, res , linkedList,used);
            linkedList.removeLast();
            used[i] = false;
        }
    }
}
