package com.hlj.arith.demo00147_组合;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：
 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
    示例:
     输入: n = 4, k = 2
     输出:
     [
     [2,4],
     [3,4],
     [2,3],
     [1,2],
     [1,3],
     [1,4],
     ]
解题思路：
*/
public class 组合 {


    @Test
    public void test(){
        System.out.println(combine(4 , 2));
    }

    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();

        dfs(1, res, linkedList, n, k);
        return res;
    }

    public void dfs(int index, List<List<Integer>> res, LinkedList<Integer> linkedList, int n, int k ) {
        if (linkedList.size() == k) {
            res.add(new ArrayList<>(linkedList));
            return;
        }

        for (int i = index; i <= n; i++) {
            linkedList.add(i);
            dfs(i + 1, res, linkedList, n, k);
            linkedList.removeLast();
        }
    }

}
