package com.hlj.arith.demo00141_数组中的全部子序列;


import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个整型数组, 你的任务是找到所有该数组的子序列,子序列的长度至少是2。
     示例:
         输入: [4, 6, 7, 7]
         输出: [[4, 6], [4, 7], [4, 6, 7], [4, 6, 7, 7], [6, 7], [6, 7, 7], [7,7], [4,7,7]]
     说明:
         给定数组的长度不会超过15。
         数组中的整数范围是 [-100,100]。
         给定数组中可能包含重复数字，相等的数字应该被视为递增的一种情况。

解题思路：
*/
public class 数组中的全部子序列 {

    @Test
    public void test(){
        int[] nums = {4, 6, 7, 7};
        System.out.println(findSubsequences(nums));
    }

    public List<List<Integer>> findSubsequences(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList();
        boolean[] used = new boolean[nums.length];
        dfs(0, nums, res, linkedList, used);
        return res;
    }

    public void dfs(int index, int[] nums, List<List<Integer>> res, LinkedList<Integer> linkedList, boolean[] used) {
        //每次进来都会放进来，不会retrun,一直到全部进入
        if (linkedList.size() > 1) {
            res.add(new ArrayList<>(linkedList));
        }

        for (int i = index; i < nums.length; i++) {
            //和全排列那里有点像，保证不是同一层在操作
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            used[i] = true;//下面+1，所以肯定不会有和当前i重复使用
            linkedList.add(nums[i]);
            dfs(i + 1, nums, res, linkedList, used);
            linkedList.removeLast();
            used[i] = false;
        }
    }

}
