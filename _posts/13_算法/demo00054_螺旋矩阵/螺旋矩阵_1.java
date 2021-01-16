package com.hlj.arith.demo00054_螺旋矩阵;

import com.hlj.arith.z_common.MatrixPrint;
import org.junit.Test;

/**
作者：HealerJean
题目： 螺旋矩阵_1
 给定一个正整数 n，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的正方形矩阵。
     示例:
         输入: 3
         输出:
         [
         [ 1, 2, 3 ],
         [ 8, 9, 4 ],
         [ 7, 6, 5 ]
         ]
解题思路：二维数组 matrix[i][j]
 从上到下、从下岛上，肯定是 j 不变 （可以理解为横坐标不变）。
 从左到右，从右到左，肯定是 i 不变 （可以理解为纵坐标不变）。
 为了按照正常思路 ，顺时针螺旋出来，肯定是 从左到右，从上到下，从右到左，从下到上，再继续 从左到右………………，
 这个时候，我们需要确定的就是边界。
 从左到右 ，肯定是在顶部的移动， i 不变，那么我们可以在初始的时候将这个顶部的i设置为变量top = 0  右面的边界是 rignt = n-1
 从上到下， 如果上面从左到右执行完毕，顶部的一行就执行完成了，top肯定向下移动了一行，所以top = top +1
            肯定是在右面移动，i 不变，我们可以这个时候  i初始就为 right  = n-1 ; matrix[k][right] 然后到底部 k最大等于bottom = n-1 ;执行完成，right = right -1


*/
public class 螺旋矩阵_1 {

    @Test
    public void test(){

        int [][] matrix = generateMatrix(3);
        MatrixPrint.print(matrix);
    }

    public int[][] generateMatrix(int n) {
        int top = 0;
        int right = n - 1;
        int bottom = n - 1;
        int left = 0;

        int num = 1;
        int sum = n * n;
        int[][] matrix = new int[n][n];
        while (num <= sum) {
            //顶部 从左到右
            for (int k = left; k <= right; k++) {
                matrix[top][k] = num;
                num++;
            }
            //顶部走完，top要加1
            top++;

            //右面 从上到下
            for (int k = top; k <= bottom; k++) {
                matrix[k][right] = num;
                num++;
            }
            //右面走完right要减一
            right--;


            //底部 从右往左
            for (int k = right; k >= left; k--) {
                matrix[bottom][k] = num;
                num++;
            }
            //底面走完right要减一
            bottom--;

            //左面 从下到上
            for (int k = bottom; k >= top; k--) {
                matrix[k][left] = num;
                num++;
            }
            left++;
        }

        return matrix;
    }


}
