package com.hlj.arith.demo00113_第一个只出现一次的字符;


import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：
 在字符串 s 中找出第一个只出现一次的字符。如果没有，返回一个单空格。 s 只包含小写字母。
 示例:
 s = "abaccdeff"
 返回 "b"

 s = ""
 返回 " "
*/
public class 第一个只出现一次的字符 {

    @Test
    public void test(){
        System.out.println(firstUniqChar("asfasdf"));
    }

    public char firstUniqChar(String s) {
        List<Map<Character, Integer>> list = new ArrayList<>();
        Map<Character, Integer> map = new HashMap<>();
        for (Character ch : s.toCharArray()){
            map.put(ch, map.getOrDefault( ch,0) +1);
        }
        for (Character ch : s.toCharArray()){
            if (map.get(ch) == 1){
                return ch;
            }
        }
        return ' ' ;
    }
}
