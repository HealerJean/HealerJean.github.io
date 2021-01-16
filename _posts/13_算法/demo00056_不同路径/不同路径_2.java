package com.hlj.arith.demo00056_不同路径;

import org.junit.Test;


/**
作者：HealerJean
题目：不同路径2 于跃考过我
 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
 现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？
 解题思路：
*/
public class 不同路径_2 {

    @Test
    public void test(){
           int[][] matrix = {
                {0, 0, 0},
                {0, 1, 0},
                {0, 0, 0}
        };
        System.out.println(uniquePathsWithObstacles(matrix));
    }

    /** 动态规划 */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        //边界问题
        if (obstacleGrid[0][0] == 1){
            return 0 ;
        }

        dp[0][0] = 1 ;
        //先确定边界的值
        for (int i = 1; i < m; i++) {
            if (obstacleGrid[i][0] == 0 ){
                dp[i][0] =  dp[i-1][0]  ;
            }
        }
        for (int j = 1; j < n; j++) {
            if (obstacleGrid[0][j] == 0 ){
                dp[0][j] = dp[0][j-1]   ;
            }
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0){
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }

}
