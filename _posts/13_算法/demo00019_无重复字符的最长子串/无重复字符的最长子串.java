package com.hlj.arith.demo00019_无重复字符的最长子串;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
作者：HealerJean
题目：无重复字符的最长子串：给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
    示例 1:
         输入: "abcabcbb"
         输出: 3
         解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
         示例 2:

         输入: "bbbbb"
         输出: 1
         解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
         示例 3:

         输入: "pwwkew"
         输出: 3
         解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
              请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 复杂度分析：
     时间复杂度 ：O(n），索引 j 将会迭代 n 次。
     空间复杂度 （HashMap）：O(n)
 */
public class 无重复字符的最长子串 {

    @Test
    public void test(){
        String s = "abcdefcadda";
        System.out.println(lengthOfLongestSubstring(s));
    }


    public int lengthOfLongestSubstring(String s) {
        //start表示无重复字符串的起始位置
        int n = s.length(), length = 0, start = 0;
        Map<Character, Integer> map = new HashMap<>();
        for (int j = 0; j < n; j++) {
            if (map.containsKey(s.charAt(j))) {
                //如果相同的数据位置比 当前无重复字符串起始位置小，则舍弃，否则表示无重复字符串起始位置将会变成当前相同位置
                start = Math.max(map.get(s.charAt(j)), start);
            }

            //j-rotateRightMethod+1 表示当前位置（j + 1）-起始位置
            length = Math.max(length, j + 1- start );
            //保存依次保存所有的数据，如果已经存在则覆盖保存成最新的
            map.put(s.charAt(j), j + 1);
        }
        return length;
    }

}
