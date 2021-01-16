package com.hlj.arith.demo00042_组合总和;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：
 给定一个数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
 candidates 中的每个数字在每个组合中只能使用一次。
 说明：所有数字（包括目标数）都是正整数。解集不能包含重复的组合。
     示例 1:
         输入: candidates = [10,1,2,7,6,1,5], target = 8,
         所求解集为:
         [
         [1, 7],
         [1, 2, 5],
         [2, 6],
         [1, 1, 6]
         ]
     示例 2:
         输入: candidates = [2,5,2,1,2], target = 5,
         所求解集为:
         [
         [1,2,2],
         [5]
         ]
解题思路：
*/
public class 组合总和_2 {

    @Test
    public void test() {

        int[] candidates = {10, 1, 2, 7, 6, 1, 5};
        int target = 8;
        System.out.println(combinationSum2(candidates, target));
    }


    public List<List<Integer>> combinationSum2(int[] candidates, int target) {

        List<List<Integer>> res = new ArrayList<>();
        //先排序，提前终止搜索
        Arrays.sort(candidates);
        Stack<Integer> stack = new Stack<>();
        boolean[] used = new boolean[candidates.length];
        dfs(target, 0, stack, candidates, res, used );
        return res;
    }


    public void dfs(int target, int index, Stack<Integer> stack, int[] candidates, List<List<Integer>> res, boolean[] used ) {
        if (target == 0) {
            res.add(new ArrayList<>(stack));
            return;
        }

        for (int i = index; i < candidates.length; i++) {
            //必须保证是不同一层
            if (i > 0 &&  candidates[i] == candidates[i-1] && !used[i-1] ){
                continue;
            }

            if (candidates[i] <= target) {
                stack.push(candidates[i]);
                used[i] = true ;
                dfs(target - candidates[i], i +1, stack, candidates, res, used);
                stack.pop();
                used[i] = false ;
            }
        }
    }

}
