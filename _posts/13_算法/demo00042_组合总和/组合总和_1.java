package com.hlj.arith.demo00042_组合总和;

import org.junit.Test;

import java.util.*;

/**
 * 作者：HealerJean
 * 题目：组合总和
 * 给定一个无重复元素的数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。candidates 中的数字可以无限制重复被选取。
 说明：

 所有数字（包括 target）都是正整数。
 解集不能包含重复的组合。 
 示例 1:

 输入: candidates = [2,3,6,7], target = 7,
 所求解集为:
 [
 [7],
 [2,2,3]
 ]
 示例 2:

 输入: candidates = [2,3,5], target = 8,
 所求解集为:
 [
   [2,2,2,2],
   [2,3,3],
   [3,5]
 ]

 * 解题思路：
 */
public class 组合总和_1 {

    @Test
    public void test() {

        int[] candidates = {2, 3, 6, 7};
        int target = 7;
        System.out.println(combinationSum(candidates, target));
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {

        // 排序是为了提前终止搜索，当然也不可以不排序
        // Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        dfs(target, 0, stack, candidates, res);
        return res;
    }


    /**
     * 深度遍历
     */
    public void dfs(int target, int index, Stack<Integer> stack, int[] candidates, List<List<Integer>> res) {
        //等于零说明结果符合要求，将栈里面的数据取出来放到结果List中去
        if (target == 0) {
            res.add(new ArrayList<>(stack));
            return;
        }


        //遍历，index为本分支上一节点的减数的下标，只往后看不往回看这样就不会有重复的了，类似于3数之和
        for (int i = index; i < candidates.length; i++) {
            //如果减数大于目标值，则差为负数，不符合结果
            if (candidates[i] <= target) {
                stack.push(candidates[i]);
                //目标值减去元素值，
                dfs(target - candidates[i], i, stack, candidates, res);
                //如果能走到这里，说明回溯已经完成了，或者有了结果，或者没有结果，因为走多了，所以要回退到上一个节点。具体debug一遍就知道了
                stack.pop();
            }
        }
    }

}
