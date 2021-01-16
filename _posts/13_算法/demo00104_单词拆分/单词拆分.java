package com.hlj.arith.demo00104_单词拆分;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：
 给定一个非空字符串 s 和一个包含非空单词列表的字典 wordDict，判定 s 是否可以被空格拆分为一个或多个在字典中出现的单词。
     说明：
         拆分时可以重复使用字典中的单词。
         你可以假设字典中没有重复的单词。
     示例 1：
         输入: s = "leetcode", wordDict = ["leet", "code"]
         输出: true
         解释: 返回 true 因为 "leetcode" 可以被拆分成 "leet code"。
     示例 2：
        输入: s = "applepenapple", wordDict = ["apple", "pen"]
        输出: true
        解释: 返回 true 因为 "applepenapple" 可以被拆分成 "apple pen apple"。
     示例 3：
        输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
        输出: false
解题思路：(你可以假设字典中没有重复的单词)，有了这句就可以拆分字符串了
*/
public class 单词拆分 {

    @Test
    public void test(){
        String str = "applepen";
        List<String> list = Arrays.asList("apple", "pen");
        System.out.println(wordBreak(str, list));
    }

    public boolean wordBreak(String s, List<String> wordDict) {
        Set<String> set = new HashSet(wordDict);
        // 字符串 s 前 i 个字符组成的字符串 s[0..i-1]  （s.substring  包头不包尾）
        boolean[] dp = new boolean[s.length() + 1];
        //起始设置为true，为了让第一个字符进入，所以i从1开始，后面会有substring(0, 1)，这样第一个字符就进入了
        dp[0] = true;
        // s.substring  包头不包尾
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                //包头不包含尾，很重要 如果 0，j-1 是true，则再判断 j 到i的情况，，
                // 然后符合的话，就表示 0 到 i -1 集合中
                if (dp[j] && set.contains(s.substring(j, i))) {
                    //i是最长，符合就不需药在执行j了
                    dp[i] = true;
                    break;
                }
            }
        }
        //表示 0 到 s.length。 包头不包尾。
        return dp[s.length()];
    }

}
