package com.hlj.arith.demo00154_子集;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：

 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
 说明：解集不能包含重复的子集。
     示例 :
     输入: nums = [1,2,3]
     输出:
         [
         [3],
         [1],
         [2],
         [1,2,3],
         [1,3],
         [2,3],
         [1,2],
         []
         ]
解题思路：
*/
public class 子集_1 {

    @Test
    public void test(){
        int[] nums = {1,2,3};
        System.out.println(subsets(nums));
    }

    public List<List<Integer>> subsets(int[] nums) {

        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        dsf(0, nums, res , linkedList);
        return res;
    }

    public void dsf(int index, int[]  nums,List<List<Integer>> res, LinkedList<Integer> linkedList){
        res.add(new ArrayList<>(linkedList));
        for (int i = index; i < nums.length; i++) {
            linkedList.add(nums[i]);
            dsf(i+1, nums, res , linkedList);
            linkedList.removeLast();
        }
    }

}
