package com.hlj.arith.demo0006_两数之和;

import org.junit.Test;

import java.util.Arrays;

/**
作者：HealerJean
题目：
 给定一个已按照升序排列 的有序数组，找到两个数使得它们相加之和等于目标数。
 函数应该返回这两个下标值 index1 和 index2，其中 index1 必须小于 index2。
 说明:
     返回的下标值（index1 和 index2）不是从零开始的。
     你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素。
 示例:
     输入: numbers = [2, 7, 11, 15], target = 9
     输出: [1,2]
     解释: 2 与 7 之和等于目标数 9 。因此 index1 = 1, index2 = 2 。
解题思路：有些像3数之和
*/
public class 两数之和2_输入有序数组 {

    @Test
    public void test(){
        int[] numbers = {2, 7, 11, 15};
        int target = 9;
        System.out.println(Arrays.toString(twoSum(numbers, target)));
    }

    public int[] twoSum(int[] numbers, int target) {
        int i = 0 ;
        int j = numbers.length-1;
        while (i < j){
           int curSum =   numbers[i] + numbers[j];
            if (target == curSum){
                return new int[]{i+1, j+1};
            }else if (curSum > target){
                j--;
            }else {
                i++ ;
            }
        }
        return new int[]{};
    }
}
