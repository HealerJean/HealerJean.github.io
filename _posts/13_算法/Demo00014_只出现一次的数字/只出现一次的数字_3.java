package com.hlj.arith.Demo00014_只出现一次的数字;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
作者：HealerJean
题目：
 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。
     示例 :
         输入: [1,2,1,3,2,5]
         输出: [3,5]
解题思路：
*/
public class 只出现一次的数字_3 {

    @Test
    public void test(){
        int[] nums = {4,1,2,1,2};
        System.out.println(singleNumber(nums));
    }

    /**
     * 算法1：
     */
    public int[] singleNumber(int[] nums) {
        int[] array = new int[2];
        int idx = 0 ;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }

        for (Integer key : map.keySet()){
            if (map.get(key) == 1){
                array[idx++] = key;
            }
        }
        return array;
    }


}
