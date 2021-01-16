package com.hlj.arith.demo00047_搜索二维矩阵;

import org.junit.Test;

/**
作者：HealerJean
题目：
 编写一个高效的算法来搜索 m x n 矩阵 matrix 中的一个目标值 target。该矩阵具有以下特性：
     每行的元素从左到右升序排列。
     每列的元素从上到下升序排列。
 现有矩阵 matrix 如下：
 {
 {1,   4,  7, 11, 15},
 {2,   5,  8, 12, 19},
 {3,   6,  9, 16, 22},
 {10, 13, 14, 17, 24},
 {18, 21, 23, 26, 30}
 }
 给定 target = 5，返回 true。
 给定 target = 20，返回 false。

解题思路：类似于2分查找

*/
public class 搜索二维矩阵 {


    @Test
    public void test() {
        int[][] matrix = {
                {1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        };
        System.out.println(searchMatrix(matrix, 23));

    }

    public boolean searchMatrix(int[][] matrix, int target) {
        int i = matrix.length - 1;
        int j = 0;
        while (i >= 0 && j < matrix[0].length){
            if (matrix[i][j] == target){
                return true;
            }else if (matrix[i][j] <target ){
                j ++ ;
            }else {
                i-- ;
            }
        }
        return false;
    }
}
