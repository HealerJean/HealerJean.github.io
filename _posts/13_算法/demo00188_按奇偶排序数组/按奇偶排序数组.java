package com.hlj.arith.demo00188_按奇偶排序数组;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个非负整数数组 A， A 中一半整数是奇数，一半整数是偶数。
 对数组进行排序，以便当 A[i] 为奇数时，i 也是奇数；当 A[i] 为偶数时， i 也是偶数。
 你可以返回任何满足上述条件的数组作为答案。
     示例：
         输入：[4,2,5,7]
         输出：[4,5,2,7]
         解释：[4,7,2,5]，[2,5,4,7]，[2,7,4,5] 也会被接受。
解题思路：
*/
public class 按奇偶排序数组 {

    @Test
    public void test(){

    }

    /**
     * 1、算法1，两次遍历
     */
    public int[] sortArrayByParityII1(int[] A) {
        int n = A.length;
        int[] res = new int[n];

        int i = 0;
        for (int x : A) {
            if (x % 2 == 0) {
                res[i] = x;
                i += 2;
            }
        }
        i = 1;
        for (int x : A) {
            if (x % 2 == 1) {
                res[i] = x;
                i += 2;
            }
        }
        return res;
    }

    /**
     * i 维护 偶数
     * j 维护 奇数
     */
    public int[] sortArrayByParityII(int[] A) {
        int n = A.length;
        int j = 1;
        for (int i = 0; i < n; i += 2) {
            if (A[i] % 2 == 1) {
                while (A[j] % 2 == 1) {
                    j += 2;
                }
                swap(A, i, j);
            }
        }
        return A;
    }

    public void swap(int[] A, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }


}
