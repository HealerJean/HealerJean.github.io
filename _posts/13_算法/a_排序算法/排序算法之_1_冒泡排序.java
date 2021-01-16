package com.hlj.arith.a_排序算法;

import org.junit.Test;

import java.util.Arrays;

/**
作者：HealerJean
题目：冒泡排序
    1. 从当前元素起，向后依次比较每一对相邻元素，若逆序则交换
    2. 对所有元素均重复以上步骤，直至最后一个元素
解题思路：
*/
public class 排序算法之_1_冒泡排序 {

    @Test
    public void test(){
        int nums[] = {49, 38, 65, 97, 76, 13, 27, 50};
        冒泡排序(nums);
        System.out.println(Arrays.toString(nums));
    }

    public void 冒泡排序(int[] nums) {
        // i=> 排序次数（最多做n-1趟排序）
        for (int i = 1; i < nums.length; i++) {
            //j，当前位置指针 j最大不能超过 str.length - i
            for (int j = 0; j < nums.length - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }
        }
    }


    public void 冒泡排序优化(int[] nums) {
        // i=> 排序次数（最多做n-1趟排序）
        for (int i = 1; i < nums.length; i++) {
            //是否发生交换
            boolean flag = false;
            //j，当前位置指针 j最大不能超过 str.length - i
            for (int j = 0; j < nums.length - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    flag = true;
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }

            //当一趟比较没有发送交换的时间表示已经有序
            if (!flag) {
                break;
            }
        }
    }


}
