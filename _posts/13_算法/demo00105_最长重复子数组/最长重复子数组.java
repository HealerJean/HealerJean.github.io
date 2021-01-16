package com.hlj.arith.demo00105_最长重复子数组;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
作者：HealerJean
题目：
 给两个整数数组 A 和 B ，返回两个数组中公共的、长度最长的子数组的长度。
 示例 1:
     输入:
     A: [1,2,3,2,1]
     B: [3,2,1,4,7]
     输出: 3
     解释: 长度最长的公共子数组是 [3, 2, 1]。
解题思路：动态规划
*/
public class 最长重复子数组 {


    @Test
    public void test(){
        int[] A = {1,2,3,2,1};
        int[] B = {3,2,1,4,7};
        System.out.println(findLength(A, B));
    }

    public int findLength(int[] A, int[] B) {

        int[][] dp = new int[A.length][B.length];

        int count   = 0 ;
        //上行
        for (int j = 0 ; j < B.length ; j++){
            if (B[j] == A[0]){
                dp[0][j]  = 1 ;
                count = 1 ;
            }
        }

        //左列
        for (int i = 0 ; i < A.length ; i++){
            if (A[i] == B[0]){
                dp[i][0]  = 1 ;
                count  = 1;
            }
        }

        //其他
        for (int i = 1;  i <  A.length ; i++){
            for (int j = 1; j < B.length; j++) {
                if (A[i] == B[j]){
                    dp[i][j] = dp[i-1][j-1] + 1 ;
                    count = Math.max(dp[i][j], count);
                }
            }
        }

        return count;
    }
}
