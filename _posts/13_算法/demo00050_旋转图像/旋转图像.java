package com.hlj.arith.demo00050_旋转图像;

import com.hlj.arith.z_common.MatrixPrint;
import org.junit.Test;

/**
作者：HealerJean
题目：旋转图像
 给定一个 n × n 的二维矩阵表示一个图像。将图像顺时针旋转 90 度。
 说明：你必须在原地旋转图像，这意味着你需要直接修改输入的二维矩阵。请不要使用另一个矩阵来旋转图像。

 示例 1:
     给定 matrix =
     [
     [1,2,3],
     [4,5,6],
     [7,8,9]
     ],

     原地旋转输入矩阵，使其变为:
     [
     [7,4,1],
     [8,5,2],
     [9,6,3]
     ]

 示例 2:
     给定 matrix =
     [
     [ 5, 1, 9,11],
     [ 2, 4, 8,10],
     [13, 3, 6, 7],
     [15,14,12,16]
     ],

     原地旋转输入矩阵，使其变为:
     [
     [15,13, 2, 5],
     [14, 3, 4, 1],
     [12, 6, 8, 9],
     [16, 7,10,11]
     ]

解题思路：此题主要是找规律，找到规律就容易了（看博客）
*/
public class 旋转图像 {

    @Test
    public void test() {
        int[][] matrix = {
                { 1,  2,  3,  4},
                { 5,  6,  7,  8},
                { 9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        MatrixPrint.print(matrix);
        rotate(matrix);
        MatrixPrint.print(matrix);
    }

    public void rotate(int[][] matrix) {
        int n = matrix.length;

        // 对于正对角线对称翻转
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int tmp = matrix[j][i];
                matrix[j][i] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
        MatrixPrint.print(matrix);

        // 竖轴镜像操作
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[i][n - j - 1];
                matrix[i][n - j - 1] = tmp;
            }
        }
    }


}
