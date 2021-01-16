package com.hlj.arith.demo00058_矩阵置零;

import com.hlj.arith.z_common.MatrixPrint;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
作者：HealerJean
题目：矩阵置零
解题思路：
*/
public class 矩阵置零 {


    @Test
    public void test(){
        int[][] matrix = {
                { 1,  0,  3,  4},
                { 5,  6,  7,  0},
                { 9, 0, 11, 12},
                {13, 14, 15, 16}
        };

        setZeroes(matrix);
        MatrixPrint.print(matrix);

    }
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        /** 存储行和列的值 */
        Set<Integer> rows = new HashSet<>();
        Set<Integer> cols = new HashSet<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    rows.add(i);
                    cols.add(j);
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rows.contains(i) || cols.contains(j)) {
                    matrix[i][j] = 0;
                }
            }
        }
    }


}
