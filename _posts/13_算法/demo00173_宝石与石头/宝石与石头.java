package com.hlj.arith.demo00173_宝石与石头;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
作者：HealerJean
题目：
 给定字符串J 代表石头中宝石的类型，和字符串 S代表你拥有的石头。 S 中每个字符代表了一种你拥有的石头的类型，你想知道你拥有的石头中有多少是宝石。
 J 中的字母不重复，J 和 S中的所有字符都是字母。字母区分大小写，因此"a"和"A"是不同类型的石头。
    注意: S 和 J 最多含有50个字母。 J 中的字符不重复。
     示例 1:
         输入: J = "aA", S = "aAAbbbb"
         输出: 3
     示例 2:
         输入: J = "z", S = "ZZ"
         输出: 0

解题思路：J用Set集合存储，遍历S判断字符在set中是否存在即可
*/
public class 宝石与石头 {


    @Test
    public void test(){
        System.out.println(numJewelsInStones("aA", "aAAbbbb"));
    }

    public int numJewelsInStones(String J, String S) {
        Set<Character> set = new HashSet<>();
        for (Character ch : J.toCharArray()){
            set.add(ch);
        }


        int count = 0 ;
        for (Character ch : S.toCharArray()){
            if (set.contains(ch)){
                count++;
            }
        }
        return count ;
    }
}
