package com.hlj.arith.demo00022_最长回文子串;

import org.junit.Test;

/**
作者：
题目：最长回文子串
 回文串：是指这个字符串无论从左读还是从右读，所读的顺序是一样的
 给定一个字符串 `s`，找到 `s` 中最长的回文子串。你可以假设 `s` 的最大长度为 1000。
解题思路：动态规划
*/
public class 最长回文子串 {

    @Test
    public void test(){
        System.out.println(longestPalindrome("banana"));
    }


    public String longestPalindrome(String s) {
        int len = s.length();
        //只有一个字符的时候，或者是空字符串
        if (len < 2) {
            return s;
        }

        // 初始化数组dp （i到j为回文），将对称点设置为true 也就是只有一个字符的情况
        int[][] dp = new int[len][len];
        for (int i = 0; i < len; i++) {
            dp[i][i] = 1;
        }

        int maxLen = 1;
        int start = 0;
        for (int j = 1; j < len; j++) {
            for (int i = j-1; i >= 0; i--) {

                if (s.charAt(i) == s.charAt(j)) {
                    //表格中观察如果 j-i < 2的时候dp[i + 1][j - 1] 是0，所以这里要特殊处理一下，
                    // j - i < 2  => aa ，这种情况
                    if (j - i < 2) {
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                  //如果i和j的字符不相等，则肯定是false
                } else {
                    dp[i][j] = 0;
                }


                // 只要 dp[i][j] == 1 成立，就表示子串 s[i, j] 是回文，此时记录回文长度和起始位置
                if (dp[i][j] == 1) {
                    int curLen = j - i + 1;
                    if (curLen > maxLen) {
                        maxLen = curLen;
                        start = i;
                    }
                }
            }
        }
        //包头不包尾
        return s.substring(start, start + maxLen);
    }

}
