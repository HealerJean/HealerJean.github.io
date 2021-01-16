package com.hlj.arith.demo00042_组合总和;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
作者：HealerJean
题目：
 找出所有相加之和为 n 的 k 个数的组合。组合中只允许含有 1 - 9 的正整数，并且每种组合中不存在重复的数字。
 说明：所有数字都是正整数。解集不能包含重复的组合。 
     示例 1:
         输入: k = 3, n = 7
         输出: [[1,2,4]]
     示例 2:
         输入: k = 3, n = 9
         输出: [[1,2,6], [1,3,5], [2,3,4]]
解题思路：
*/
public class 组合总和_3 {

    @Test
    public void test() {

        System.out.println(combinationSum3(2, 18));
    }


    public List<List<Integer>> combinationSum3(int k, int n) {

        List<List<Integer>> res = new ArrayList<>();
        //先排序，提前终止搜索
        Stack<Integer> stack = new Stack<>();

        dfs(1,n, k, stack, res);
        return res;
    }


    public void dfs(int index, int target, int k, Stack<Integer> stack, List<List<Integer>> res) {
        // 满足 数量为 k ， 和为 n（target）
        if (stack.size() == k && target == 0 ) {
            res.add(new ArrayList<>(stack));
            return;
        }

        for (int i = index; i <= 9; i++) {
            if (i <= target) {
                stack.push(i);
                dfs(i + 1, target - i,k, stack, res);
                stack.pop();
            }
        }
    }

}
