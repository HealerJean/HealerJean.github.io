package com.hlj.arith.Demo00014_只出现一次的数字;

import org.junit.Test;

import java.util.*;

/**
 作者：HealerJean
 题目：只出现一次的数字
 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 说明：你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 示例 1:
     输入: [2,2,1]
     输出: 1
 示例 2:
     输入: [4,1,2,1,2]
     输出: 4
 解题思路：异或特性，不相同为1
 除了只出现1次的，其它都出现2次，所以其他的异或后肯定为  0 ，全部异或后 最后的结果就是唯一的那个
 */
public class 只出现一次的数字_1 {


    @Test
    public void test(){
        int[] nums = {4,1,2,1,2};
        System.out.println(singleNumber1(nums));
        System.out.println(singleNumber(nums));
    }

    /**
     * 算法1：ArrayList
     */
    public int singleNumber1(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (list.contains(nums[i])) {
                list.remove((Integer) nums[i]);
            } else {
                list.add(nums[i]);
            }
        }
        return list.get(0);
    }

    /**
     * 算法2：HashMap
     */
    public int singleNumber2(int[] nums) {
        //map收集每个数字出现的个数
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) +1);
        }
        //找出数量为1的个数
        for (Integer key : map.keySet()){
            if (map.get(key) == 1){
                return key;
            }
        }
        return 0 ;
    }


    /**
     * 算法2：官方
     */
    public int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result = result ^ num;
        }
        return result;
    }
}

