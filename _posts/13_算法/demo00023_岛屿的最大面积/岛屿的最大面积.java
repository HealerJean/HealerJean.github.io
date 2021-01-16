package com.hlj.arith.demo00023_岛屿的最大面积;


import org.junit.Test;


/**
 * 作者：HealerJean
 * 题目：岛屿的最大面积
 * 解题思路：遍历二维数组，如果为1，则进行上下左右遍历。注意边界；
 */
public class 岛屿的最大面积 {

    @Test
    public void test() {
        int[][] array = {
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}
        };


    }

    public int maxAreaOfIsland(int[][] grid) {
        int bigSize = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                //当为0的时候就不会进入
                if (grid[i][j] == 1) {
                    bigSize = Math.max(dfs(i, j, grid), bigSize);
                }
            }
        }
        return bigSize;
    }

    /**
     * 从某个节点开始扩散
     */
    public int dfs(int i, int j, int[][] grid) {
        if (i >= 0 && i < grid.length &&
            j >= 0 && j < grid[0].length &&
            grid[i][j] == 1) {
            //防止重复执行 kuosan方法。防止重复执行 kuosan方法。
            grid[i][j] = 0;

            //每次进入就 + 1
            int num = dfs(i - 1, j, grid) +
                    dfs(i + 1, j, grid) +
                    dfs(i, j - 1, grid) +
                    dfs(i, j + 1, grid) +
                    1;
            return num;
        }
        return 0;
    }


}
