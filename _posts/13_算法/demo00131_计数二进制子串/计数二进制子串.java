package com.hlj.arith.demo00131_计数二进制子串;


import org.junit.Test;

/**
作者：HealerJean
题目：
 给定一个字符串 s，计算具有相同数量0和1的非空(连续)子字符串的数量，并且这些子字符串中的所有0和所有1都是组合(挨着)在一起的。
 重复出现的子串要计算它们出现的次数。
 注意：s.length 在1到50,000之间。s 只包含“0”或“1”字符。
     示例 1 :
         输入: "001100111"
         输出: 6
         解释: 有6个子串具有相同数量的连续1和0：“0011”，“01”，“1100”，“10”，“0011” 和 “01”。
         请注意，一些重复出现的子串要计算它们出现的次数。
         另外，“00110011”不是有效的子串，因为所有的0（和1）没有组合在一起。
     示例 2 :
         输入: "10101"
         输出: 4
         解释: 有4个子串：“10”，“01”，“10”，“01”，它们具有相同数量的连续1和0。
解题思路：
 我们可以将字符串 ss 按照 00 和 11 的连续段分组，存在 counts 数组中，例如 s=00111011，可以得到这样的 counts 数组  counts={2,3,1,2}。
 这里 counts 数组中两个相邻的数一定代表的是两种不同的字符。假设 counts 数组中两个相邻的数字为 u 或者 v，它们对应着 u 个 0 和 v 个 1，或者 u 个 1 和 v 个 0。
 它们能组成的满足条件的子串数目为  min{u,v}，即一对相邻的数字对答案的贡献。
 我们只要遍历所有相邻的数对，求它们的贡献总和，即可得到答案。

例如 s = 00111011，可以得到这样的 counts 数组：counts={2,3,1,2}。与这一段连续相同字符数的最小值：即分别为 2，1，1，相加 =  4。
例如 s = 00110011，可以得到这样的 counts 数组：counts={2,2,2,2}。与这一段连续相同字符数的最小值：即分别为 2，2，2，相加 =  6。


 */
public class 计数二进制子串 {

    @Test
    public void test() {
        System.out.println(countBinarySubstrings("00111011"));
    }

    public int countBinarySubstrings(String s) {
        int res = 0;
        int idx = 0;
        //用来临时存储上一个数字出现的个数
        int preCount = 0;
        while (idx < s.length()) {
            char cur = s.charAt(idx);
            //这里从0开始，这样的话，会加上cur的个数
            int curCount = 0;
            while (idx < s.length() && s.charAt(idx) == cur) {
                idx++;
                curCount++;
            }
            //第一次进入preCount为0
            res += Math.min(preCount, curCount);
            //走完一轮了，经当前的个数赋值给前面的
            preCount = curCount;
        }
        return res;
    }
}
