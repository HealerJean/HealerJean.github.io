package com.hlj.arith.demo00135_01矩阵;


import org.junit.Test;


/**
作者：HealerJean
题目：
 给定一个由 0 和 1 组成的矩阵，找出每个元素到最近的 0 的距离。
 两个相邻元素间的距离为 1 。
     注意:
         给定矩阵的元素个数不超过 10000。
         给定矩阵中至少有一个元素是 0。
         矩阵中的元素只在四个方向上相邻: 上、下、左、右。
     示例 1:
         输入:
         0 0 0
         0 1 0
         0 0 0
         输出:
         0 0 0
         0 1 0
         0 0 0
     示例 2:
         输入:
         0 0 0
         0 1 0
         1 1 1
         输出:
         0 0 0
         0 1 0
         1 2 1

解题思路：从四面八方进攻，左上角和左下角同时出发
*/
public class _01矩阵 {

    @Test
    public void test() {
        // int[][] matrix = {
        //         {0, 0, 0},
        //         {0, 1, 0},
        //         {1, 1, 1}
        // };
        int[][] matrix = {
                {1, 0, 1},
                {1, 1, 1},
                {1, 1, 1}
        };
        updateMatrix(matrix);
    }

    public int[][] updateMatrix(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                //如果当前有数据则相对来说是比较大的,10000（如果都为1的话，则没有意义，所以后面这个值也就是当无意义的时候设定值）
                dp[i][j] = matrix[i][j] == 0 ? 0 : 10000;
            }
        }

        // 从左上角开始
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i > 0) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + 1);
                }
                if (j > 0) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + 1);
                }
            }
        }
        // 从右下角开始
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (i < m - 1) {
                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][j] + 1);
                }
                if (j < n - 1) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][j + 1] + 1);
                }
            }
        }
        return dp;
    }

}
