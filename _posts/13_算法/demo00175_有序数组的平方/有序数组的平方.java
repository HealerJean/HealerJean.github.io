package com.hlj.arith.demo00175_有序数组的平方;

import org.junit.Test;

import java.util.Arrays;

/**
作者：HealerJean
题目：
 给定一个按非递减顺序排序的整数数组 A，返回每个数字的平方组成的新数组，要求也按非递减顺序排序。
 示例 1：
     输入：[-4,-1,0,3,10]
     输出：[0,1,9,16,100]
 示例 2：
     输入：[-7,-3,2,3,11]
     输出：[4,9,9,49,121]
解题思路：
*/
public class 有序数组的平方 {

    @Test
    public void test() {
        int[] A = {-4, -1, 0, 3, 10};
        System.out.println(Arrays.toString(sortedSquares(A)));
        System.out.println(Arrays.toString(sortedSquares2(A)));
    }

    /**
     * 方法1：简单粗暴
     */
    public int[] sortedSquares(int[] A) {

        for (int i = 0; i < A.length; i++) {
            A[i] = A[i] * A[i];
        }
        Arrays.sort(A);
        return A;
    }


    /**
     * 方法2：双指针
     */
    public int[] sortedSquares2(int[] A) {
        int[] res = new int[A.length];

        //因为不能确定原始数组的负数的绝对值和正数最大值的绝对值，所以我们先保存大的值，也就是从后往前添加
        int index = A.length - 1;
        int right = A.length - 1;
        int left = 0;
        while (left <= right){
            if (A[left] * A[left] > A[right] * A[right]) {
                res[index] = A[left] * A[left];
                left++;
            } else {
                res[index] = A[right] * A[right];
                right--;
            }
        }
        //每次放入东西index要减去
        index--;
        return res;
    }


}
