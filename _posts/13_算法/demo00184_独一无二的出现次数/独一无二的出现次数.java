package com.hlj.arith.demo00184_独一无二的出现次数;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：
 给你一个整数数组 arr，请你帮忙统计数组中每个数的出现次数。
 如果每个数的出现次数都是独一无二的，就返回 true；否则返回 false。
     示例 1：
         输入：arr = [1,2,2,1,1,3]
         输出：true
         解释：在该数组中，1 出现了 3 次，2 出现了 2 次，3 只出现了 1 次。没有两个数的出现次数相同。
     示例 2：
         输入：arr = [1,2]
         输出：false
     示例 3：
         输入：arr = [-3,0,1,-3,1,1,1,-3,10,0]
         输出：true
解题思路：
*/
public class 独一无二的出现次数 {


    @Test
    public void test() {
        // int[] arr = {1, 2, 2, 1, 1, 3};
        int[] arr = {1,2} ;
        // int[] arr = {-3,0,1,-3,1,1,1,-3,10,0} ;
        System.out.println(uniqueOccurrences(arr));
    }

    /**
     * 算法1：
     */
    public boolean uniqueOccurrences(int[] arr) {
        // 1、map存放次数
        Map<Integer, Integer>  map = new HashMap<>();
        for (int i = 0; i < arr.length; i++) {
            map.put(arr[i], map.getOrDefault(arr[i], 0)+1);
        }


        // 2、set 用来计算数量，如果重复则返回false
        Set<Integer> set = new HashSet<>();
        for (Integer value: map.values()){
            if (!set.add(value)){
                return false;
            }
        }
        return true;
    }


}
