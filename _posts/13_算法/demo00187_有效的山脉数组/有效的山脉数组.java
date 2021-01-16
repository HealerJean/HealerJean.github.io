package com.hlj.arith.demo00187_有效的山脉数组;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个整数数组 A，如果它是有效的山脉数组就返回 true，否则返回 false。
 让我们回顾一下，如果 A 满足下述条件，那么它是一个山脉数组：
 A.length >= 3
 在 0 < i < A.length - 1 条件下，存在 i 使得：
     A[0] < A[1] < ... A[i-1] < A[i],  A[i] > A[i+1] > ... > A[A.length - 1]
     示例 1：
         输入：[2,1]
         输出：false
     示例 2：
         输入：[3,5,5]
         输出：false
     示例 3：
         输入：[0,3,2,1]
         输出：true
 解题思路：
*/
public class 有效的山脉数组 {

    @Test
    public void test(){
        // int A[] = {3, 5, 5};
        // int A[] = {0, 3, 2, 1};
        int A[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        System.out.println(validMountainArray(A));
    }

    public boolean validMountainArray(int[] A) {
        if (A.length < 3) {
            return false;
        }

        int index = 0;
        while (index < A.length - 1 && A[index] < A[index + 1]) {
                index++;
        }

        //如果没走，或者是递增的则直接out
        if (index == 0  || index == A.length - 1) {
            return false;
        }

        while (index < A.length - 1 && A[index] > A[index + 1]) {
                index++;
        }

        //如果走完了就是true
        return index == A.length - 1;
    }

}
