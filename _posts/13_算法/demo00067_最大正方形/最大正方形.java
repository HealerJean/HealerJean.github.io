package com.hlj.arith.demo00067_最大正方形;

import org.junit.Test;

/**
作者：HealerJean
题目：最大正方形
 在一个由 0 和 1 组成的二维矩阵内，找到只包含 1 的最大正方形，并返回其面积。
 示例:
     输入:
     1 0 1 0 0
     1 0 1 1 1
     1 1 1 1 1
     1 0 0 1 0
     输出: 4
解题思路：
*/
public class 最大正方形 {

    @Test
    public void test(){
        char[][] matrix = {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        };
        System.out.println(maximalSquare(matrix));
    }

    public int maximalSquare(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }

        int maxLen = 0;
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] dp = new int[m][n];


        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') {
                    //最左面的一列和上面的一行 先确定，不需要走函数。它是边界
                    if (i == 0 || j == 0) {
                        dp[i][j] = 1;
                    } else {
                        //上 左 选最小的值
                        int upDownMin = Math.min(dp[i - 1][j], dp[i][j - 1]) ;
                        int topLeft = dp[i - 1][j - 1] ;

                        //从左上角，上面，下面，选择最小的数字 然后 + 1 。代表当前位置矩形的最大长度
                        dp[i][j] = Math.min(upDownMin, topLeft) + 1;
                    }
                    maxLen = Math.max(maxLen, dp[i][j]);
                }
            }
        }

        return maxLen * maxLen;
    }

}
