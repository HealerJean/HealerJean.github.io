package com.hlj.arith.demo00124_魔术索引;

import org.junit.Test;

/**
作者：HealerJean
题目：
 魔术索引。 在数组A[0...n-1]中，有所谓的魔术索引，满足条件A[i] = i。给定一个有序整数数组，编写一种方法找出魔术索引，若有的话，在数组A中找出一个魔术索引，如果没有，则返回-1。若有多个魔术索引，返回索引值最小的一个。
 提示:  nums长度在[1, 1000000]之间
     示例1:
         输入：nums = [0, 2, 3, 4, 5]
         输出：0
         说明: 0下标的元素为0
     示例2:
         输入：nums = [1, 1, 1]
         输出：1

解题思路：
*/
public class 魔术索引 {

    @Test
    public void test(){
        int[] nums = {0, 2, 3, 4, 5};
        System.out.println(findMagicIndex(nums));
    }

    public int findMagicIndex(int[] nums) {

        for (int i = 0; i < nums.length; i++) {
            if (i == nums[i]){
                return i;
            }
        }
        return -1;
    }
}
