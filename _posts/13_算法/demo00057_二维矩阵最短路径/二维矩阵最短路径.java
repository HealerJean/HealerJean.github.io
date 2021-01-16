package com.hlj.arith.demo00057_二维矩阵最短路径;

import org.junit.Test;

/**
作者：HealerJean
题目：二维矩阵最短路径
 给定一个矩阵m，从左上角开始每次只能向右或者向下走，最后到达右下角的位置，路径上所有的数字累加起来就是路径和，返回所有路径中最小的路径和。
    例子：
         给定m如下：
         1 3 5 9
         8 1 3 4
         5 0 6 1
         8 8 4 0
         路径1,3,1,0,6,1,0是所有路径中路径和最小的，所以返回12。
解题思路：
*/
public class 二维矩阵最短路径 {

    @Test
    public void test(){
        int[][] matrix = {
                {1, 3, 5, 9},
                {8, 1, 3, 4},
                {8, 8, 4, 0}
        };
        System.out.println(minPathSum(matrix));
    }


    public int minPathSum(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int dp[][] = new int[m][n];
        dp[0][0] = matrix[0][0];
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + matrix[i][0];
        }
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + matrix[0][j];
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + matrix[i][j];
            }
        }

        return dp[m - 1][n - 1];
    }

}
