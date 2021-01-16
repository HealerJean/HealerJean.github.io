package com.hlj.arith.demo00054_螺旋矩阵;

import com.hlj.arith.z_common.MatrixPrint;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
作者：HealerJean
题目： 螺旋矩阵_2
 给定一个包含 m x n 个元素的矩阵（m 行, n 列），请按照顺时针螺旋顺序，返回矩阵中的所有元素。
     示例 1:
         输入:
         [
         [ 1, 2, 3 ],
         [ 4, 5, 6 ],
         [ 7, 8, 9 ]
         ]
         输出: [1,2,3,6,9,8,7,4,5]
     示例 2:
         输入:
         [
         [1, 2, 3, 4],
         [5, 6, 7, 8],
         [9,10,11,12]
         ]
         输出: [1,2,3,4,8,12,11,10,9,5,6,7]


解题思路：
*/
public class 螺旋矩阵_2 {

    @Test
    public void test(){

        int[][] matrix = {
                { 1,  2,  3,  4},
                { 5,  6,  7,  8},
                { 9, 10, 11, 12}
        };
        MatrixPrint.print(matrix);
        System.out.println(spiralOrder(matrix));
    }

    public List<Integer> spiralOrder(int[][] matrix) {
        // 1、判空
        List<Integer> list = new ArrayList<>();
        if (matrix.length==0){
            return list;
        }
        if (matrix[0].length ==0){
            return list;
        }

        int count = (matrix.length )* (matrix[0].length);
        int num = 1 ;
        int top = 0 ;
        int right = matrix[0].length-1;
        int bottom = matrix.length -1 ;
        int left = 0;
        while (num <= count){
            //在顶部从左到右
            for (int k = left ; k <= right ; k++){
                list.add(matrix[top][k]);
                num++;
            }
            top++;

            if (num > count){
                break;
            }
            //在右面从上到下
            for (int k = top ; k <= bottom ; k++){
                list.add(matrix[k][right]);
                num++;
            }
            right--;

            if (num > count){
                break;
            }
            //在底部从右到左
            for (int k = right; k >= left ; k--){
               list.add(matrix[bottom][k]);
               num++;
            }
            bottom--;

            if (num > count){
                break;
            }
            for (int k = bottom ; k >= top ; k--){
                list.add(matrix[k][left]);
                num++;
            }
            left++;
        }

        return list;
    }



}
