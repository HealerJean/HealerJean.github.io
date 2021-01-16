package com.hlj.arith.demo00039_下一个排列;

import org.junit.Test;

import java.util.Arrays;
/**
作者：HealerJean
题目：下一个排列
    实现获取下一个排列的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列。如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
    注意：必须原地修改，只允许使用额外常数空间。

举例：
 1,2,3 → 1,3,2
 3,2,1 → 1,2,3
 1,1,5 → 1,5,1
 1,5,8,4,7,6,5,3,1 ->1,5,8,5,7,6,4,3,1
 解题思路：
*/
public class 下一个排列 {


    @Test
    public void test() {
        // int[] nums = new int[]{3, 2, 1};
        int[] nums = new int[]{1, 2, 3, 2};
        nextPermutation(nums);
        System.out.println(Arrays.toString(nums));
    }

    public void nextPermutation(int[] nums) {
        int i = nums.length - 2;
        //第一次比较
        while (i >= 0 &&  nums[i]>=nums[i + 1] ) {
            i--;
        }

        // 当i >=0 的时候，代表着肯定有下一个排列，那么从后往前找到一个数字比i的位置打就可以
        if (i >= 0) {
            int j = nums.length - 1;
            while (nums[j] <= nums[i]) {
                j--;
            }
            swap(nums, i, j);
        }

        //将 i后面的数组进行反转（此处就包括了i == 0 的情况）
        reverse(nums, i + 1);
    }



    private void reverse(int[] nums, int start) {
        int i = start, j = nums.length - 1;
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }


}
