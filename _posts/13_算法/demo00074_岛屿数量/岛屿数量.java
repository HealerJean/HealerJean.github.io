package com.hlj.arith.demo00074_岛屿数量;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 岛屿总是被水包围，并且每座岛屿只能由水平方向或竖直方向上相邻的陆地连接形成。
 此外，你可以假设该网格的四条边均被水包围。
 示例 1:
     输入:
     11110
     11010
     11000
     00000
     输出: 1
 示例 2:
     输入:
     11000
     11000
     00100
     00011
     输出: 3
     解释: 每座岛屿只能由水平和/或竖直方向上相邻的陆地连接而成。
解题思路：
*/
public class 岛屿数量 {


    @Test
    public void test() {
        char[][] matrix = {
                {'1', '1', '1', '1', '0'},
                {'1', '1', '0', '1', '0'},
                {'1', '1', '0', '0', '0'},
                {'0', '0', '0', '0', '0'}
        };

        System.out.println(numIslands(matrix));
    }


    public int numIslands(char[][] grid) {

        int count = 0 ;
        for (int i = 0 ; i < grid.length ; i++){
            for (int j = 0 ; j < grid[0].length ; j++){
                if (grid[i][j] == '1'){
                    //只要能进入就代表肯定可以
                    count ++ ;
                    kuosan(i, j, grid);
                }
            }
        }
        return count ;
    }

    public void kuosan(int i, int j, char[][] grid) {
        if (i >= 0 && i < grid.length
                && j >= 0 && j < grid[0].length
                && grid[i][j] == '1') {

            //设为0，下次不进入了
            grid[i][j] = 0 ;
            kuosan(i-1 , j, grid);
            kuosan(i+1, j, grid);
            kuosan(i,j-1,grid);
            kuosan(i,j+1,grid);
        }
    }

}
