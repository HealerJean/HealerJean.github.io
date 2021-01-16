package com.hlj.arith.demo00063_最大矩形;

import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

/**
作者：HealerJean
题目：给定一个仅包含 0 和 1 的二维二进制矩阵，找出只包含 1 的最大矩形，并返回其面积。
     示例:
         输入:
         [
         ['1','0','1','0','0'],
         ['1','0','1','1','1'],
         ['1','1','1','1','1'],
         ['1','0','0','1','0']
         ]
         输出: 6
 解题思路：

 {'1', '0', '1', '0', '0'},
 {'1', '0', '1', '1', '1'},
 {'1', '1', '1', '1', '1'},
 {'1', '0', '0', '1', '0'}

 变成如下的，求矩形
 {1, 0, 1, 0, 0},
 {1, 0, 3, 2, 1},
 {5, 4, 3, 2, 1},
 {1, 0, 0, 1, 0}
*/
public class 最大矩形 {

    @Test
    public void test(){
        char[][] matrix = {
                {'1', '0', '1', '0', '0'},
                {'1', '0', '1', '1', '1'},
                {'1', '1', '1', '1', '1'},
                {'1', '0', '0', '1', '0'}
        };

        System.out.println(maximalRectangle(matrix));
    }
    public int maximalRectangle(char[][] matrix) {
        //边界条件
        if(null == matrix || 0 == matrix.length) {
            return 0;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] dp = new int[m][n];

        //从倒数第一列开始 dp[i][n-1]
        for (int i = 0; i < m; i++) {
            if (matrix[i][n-1] ==  '1'){
                dp[i][n - 1] = 1 ;
            }
        }

        //从倒数第二列开始继续往前推，数组默认就是0，所以只要求出1的就可以了
        for (int i = 0; i < m; i++) {
            for (int j = n - 2; j >= 0; j--) {
                if (matrix[i][j] == '1'){
                    dp[i][j] =  1 + dp[i][j + 1];
                }
            }
        }

        int res = 0;

        //以列推进。计算柱状最大面积,将 行坐标i放到栈里面去
        for(int j = 0; j < n; j++) {
            Stack<Integer> stack = new Stack<>();
            stack.push(-1);
            for(int i = 0; i < m; i++) {
                while (stack.peek() != -1 && dp[i][j] < dp[stack.peek()][j]) {
                    res = Math.max(res, dp[stack.pop()][j] * (i - stack.peek() -1));
                }
                stack.push(i);
            }
            while (stack.peek() != -1){
                res = Math.max(res,  dp[stack.pop()][j] * (m - stack.peek() -1));
            }
        }
        return res;
    }
}
