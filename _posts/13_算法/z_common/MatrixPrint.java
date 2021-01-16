package com.hlj.arith.z_common;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName MatrixPrint
 * @date 2020/4/21  18:04.
 * @Description
 */
public class MatrixPrint {


    @Test
    public void test(){

        int[][] matrix = {
                { 1,  2,  3,  4},
                { 5,  6,  7,  8},
                { 9, 10, 11, 12},
                {13, 14, 15, 16}
        };
    }

    public static void  print(int[][] matrix ){
        for (int i = 0 ; i < matrix.length ; i++){
            for (int j = 0 ;j < matrix[0].length ; j++){
                if (matrix[i][j] < 10 ){
                    System.out.printf(" " + matrix[i][j] + ", ");
                }else {
                    System.out.printf( matrix[i][j] + ", ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
