package com.hlj.arith.demo00106_有序矩阵中第K小的元素;

import com.hlj.arith.z_common.ArraryPrint;
import org.junit.Test;

/**
作者：HealerJean
题目：有序矩阵中第K小的元素
 给定一个 n x n 矩阵，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素。
 请注意，它是排序后的第 k 小元素，而不是第 k 个不同的元素。
     示例：
         matrix = [
         [ 1,  5,  9],
         [10, 11, 13],
         [12, 13, 15]
         ],
         k = 8,
         返回 13。
解题思路：
*/
public class 有序矩阵中第K小的元素 {

    @Test
    public void test() {

        int[][] matrix = {
                {1, 5, 9},
                {10, 11, 13},
                {12, 13, 15}
        };

        System.out.println(kthSmallest(matrix, 8));
    }


    public int kthSmallest(int[][] matrix, int k) {
        //其中每行和每列元素均按升序排序 ,所以最小值在左上交，最大值在右上角
        int left = matrix[0][0];
        int right = matrix[matrix.length - 1][matrix.length - 1];
        while (left < right) {
            int mid =   ((right + left) / 2);
            //计算小于等于中位数的个数有多少个
            int count = moreThanMid(mid, matrix);
            if (count < k) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }



    public int moreThanMid(int mid, int[][] matrix) {
        int i = matrix.length -1 ;
        int j = 0;
        int count = 0;
        while (i >= 0 && j < matrix.length) {
            if (matrix[i][j] <= mid) {
                // i + 1 为当前满足if的列上个数
                count += i + 1;
                j++;
            } else {
                i--;
            }
        }
        return count;
    }
}
