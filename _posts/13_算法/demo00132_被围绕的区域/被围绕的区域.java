package com.hlj.arith.demo00132_被围绕的区域;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个二维的矩阵，包含 'X' 和 'O'（字母 O）。
 找到所有被 'X' 围绕的区域，并将这些区域里所有的 'O' 用 'X' 填充。
 示例:
     X X X X
     X O O X
     X X O X
     X O X X
     运行你的函数后，矩阵变为：
     X X X X
     X X X X
     X X X X
     X O X X
     解释:
     被围绕的区间不会存在于边界上，换句话说，任何边界上的 'O' 都不会被填充为 'X'。 任何不在边界上，或不与边界上的 'O' 相连的 'O' 最终都会被填充为 'X'。如果两个元素在水平或垂直方向相邻，则称它们是“相连”的。
解题思路：从边界出发，找到所有不会被填充的O，变成Ｔ，然后再遍历一次即可得到答案
*/
public class 被围绕的区域 {

    @Test
    public void test() {
        char[][] board = {
                {'X', 'X', 'X', 'X'},
                {'X', 'O', 'O', 'X'},
                {'X', 'X', 'O', 'X'},
                {'X', 'O', 'X', 'X'}
        };
        solve(board);
    }

    /**
     * 从边界出发，找到所有不会被填充的O，变成Ｔ，然后再遍历一次即可得到答案
     */
    public void solve(char[][] board) {

        //防止为空
        if (board.length == 0) {
            return;
        }

        //第一行 i = 0
        for (int j = 0; j < board[0].length; j++) {
            if (board[0][j] == 'O') {
                dfs(board, 0, j);
            }
        }

        //最后一行 i = board.length-1
        for (int j = 0; j < board[0].length; j++) {
            if (board[board.length - 1][j] == 'O') {
                dfs(board, board.length - 1, j);
            }
        }

        //第一列
        for (int i = 0; i < board.length; i++) {
            if (board[i][0] == 'O') {
                dfs(board, i, 0);
            }
        }
        //最后一列
        for (int i = 0; i < board.length; i++) {
            if (board[i][board[0].length - 1] == 'O') {
                dfs(board, i, board[0].length - 1);
            }
        }

        //将 T变成O ，将O变成X
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 'T') {
                    board[i][j] = 'O';
                } else if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
            }
        }

    }

    public void dfs(char[][] board, int i, int j) {
        if (i >= 0 && i < board.length
                && j >= 0 && j < board[0].length
                && board[i][j] == 'O') {
            board[i][j] = 'T';
            dfs(board, i - 1, j);
            dfs(board, i + 1, j);
            dfs(board, i, j - 1);
            dfs(board, i, j + 1);
        }
    }


}



