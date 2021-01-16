package com.hlj.arith.demo00118_判断子序列;


import org.junit.Test;

/**
作者：HealerJean
题目：
 给定字符串 s 和 t ，判断 s 是否为 t 的子序列。
 你可以认为 s 和 t 中仅包含英文小写字母。字符串 t 可能会很长（长度 ~= 500,000），而 s 是个短字符串（长度 <=100）。
 字符串的一个子序列是原始字符串删除一些（也可以不删除）字符而不改变剩余字符相对位置形成的新字符串。（例如，"ace"是"abcde"的一个子序列，而"aec"不是）。
     示例 1:
         s = "abc", t = "ahbgdc"
     返回 true.
         示例 2:
         s = "axc", t = "ahbgdc"
         返回 false.
解题思路：
*/
public class 判断子序列 {

    @Test
    public void test() {
        // System.out.println(isSubsequence("abc", "ahbgdc"));
        System.out.println(isSubsequence("axc", "ahbgdc"));

    }

    public boolean isSubsequence(String s, String t) {

        int i = 0;
        int j = 0;
        while (i < s.length() && j < t.length()) {
            if (t.charAt(j) == s.charAt(i)) {
                i++;
            }
            j++;
        }
        return i == s.length();
    }
}
