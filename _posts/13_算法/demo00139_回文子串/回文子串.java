package com.hlj.arith.demo00139_回文子串;

import org.junit.Test;


/**
作者：HealerJean
题目：
 给定一个字符串，你的任务是计算这个字符串中有多少个回文子串。
 具有不同开始位置或结束位置的子串，即使是由相同的字符组成，也会被视作不同的子串。
 提示：输入的字符串长度不会超过 1000 。
     示例 1：
         输入："abc"
         输出：3
         解释：三个回文子串: "a", "b", "c"
     示例 2：
         输入："aaa"
         输出：6
         解释：6个回文子串: "a", "a", "a", "aa", "aa", "aaa"

解题思路：
*/
public class 回文子串 {

    @Test
    public void test(){
        System.out.println(countSubstrings("aaa"));
    }

    public int countSubstrings(String s) {
        int[][] dp = new int[s.length()][s.length()] ;
        int res = 0 ;
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = 1 ;
            res++;
        }

        for (int j = 1; j < s.length(); j++) {
            for (int i = j - 1; i >= 0; i--) {
                if (s.charAt(i) == s.charAt(j)){
                    if (j-i < 2 ){
                        dp[i][j] = 1 ;
                    }else {
                        dp[i][j] = dp[i+1][j-1];
                    }

                    if (dp[i][j] == 1){
                        res++;
                    }
                }
            }
        }
        return res;
    }





}
