package com.hlj.arith.demo00049_全排列;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：全排列_2
 给定一个可包含重复数字的序列，返回所有不重复的全排列。
 示例:
     输入: [1,1,2]
     输出:
     [
     [1,1,2],
     [1,2,1],
     [2,1,1]
     ]
 解题思路：
*/
public class 全排列_2 {

    @Test
    public void test(){
        int nums[] = {1,1,2};
        System.out.println(permuteUnique(nums));
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        //切记必须要先排序啊！！！！！！这样只有相邻的才可能相等，才可以判断去除！！！！！
        Arrays.sort(nums);
        List<List<Integer>> list = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        dfs(nums, used, new LinkedList<>(), list);
        return list;
    }

    private void dfs(int[] nums, boolean[] used, LinkedList array, List<List<Integer>> list) {
        if (array.size() == nums.length) {
            list.add(new ArrayList<>(array));
            return;
        }

        for (int i = 0; i < nums.length; ++i) {
            //当前值用过了(从开始回溯，肯定会重复使用，所以要判断) 或
            //当前值等于前一个值： 两种情况：（归根接地，就是要保证 同一层的时候，num[n-1] 如果不等于true了，continue）
            //1 [i-1] = false,没有被使用, 说明回溯到了同一层 ，这个时候满足了 nums[i] == nums[i - 1]，肯定是重复，所以continue
            //2 [i-1] = true, 用过了 说明此时在num[i-1]的下一层，这种情况可以往下走，不需要continue
            if (used[i] || (i > 0  && nums[i] == nums[i - 1] && !used[i - 1])) {
                continue;
            }

            array.add(nums[i]);
            used[i] = true;
            dfs(nums, used, array, list);
            // 回溯部分的代码，和 dfs 之前的代码是对称的
            used[i] = false;
            array.removeLast();
        }
    }


}
