package com.hlj.arith.demo00140_重复的子字符串;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个非空的字符串，判断它是否可以由它的一个子串重复多次构成。给定的字符串只含有小写英文字母，并且长度不超过10000。
     示例 1:
         输入: "abab"
         输出: True
         解释: 可由子字符串 "ab" 重复两次构成。
         示例 2:
         输入: "aba"
         输出: False
     示例 3:
         输入: "abcabcabcabc"
         输出: True
         解释: 可由子字符串 "abc" 重复四次构成。 (或者子字符串 "abcabc" 重复两次构成。)
解题思路：
*/
public class 重复的子字符串 {

    @Test
    public void test(){
        System.out.println(repeatedSubstringPattern("abcabcabcabc"));
        System.out.println(repeatedSubstringPattern("ababab"));

    }

    public boolean repeatedSubstringPattern(String s) {
        int len = s.length();
        //i 重复的长度，最长只能为len的一半
        for (int i = 1; i * 2 <= len; ++i) {
            //当等于0的时候才有可能存在,肯定是i长度的整数倍数
            if (len % i == 0) {
                boolean match = true;
                //j 试探能够到字符串的尾部，所以 j<len
                for (int j = i; j < len; ++j) {
                    //只要有一个不成立则退出
                    if (s.charAt(j) != s.charAt(j - i)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return true;
                }
            }
        }
        return false;
    }
}
