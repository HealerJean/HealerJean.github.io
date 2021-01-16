package com.hlj.arith.demo00090_把数字翻译成字符串;

import org.junit.Test;

/**
作者：HealerJean
题目：
给定一个数字，我们按照如下规则把它翻译为字符串：0 翻译成 “a” ，1 翻译成 “b”，……，11 翻译成 “l”，……，25 翻译成 “z”。一个数字可能有多个翻译。请编程实现一个函数，用来计算一个数字有多少种不同的翻译方法。
提示：0 <= num < 231
示例 1:
    输入: 12258
    输出: 5
解释: 12258有5种不同的翻译，分别是"bccfi", "bwfi", "bczi", "mcfi"和"mzi"
解题思路：
 */
public class 把数字翻译成字符串 {

    @Test
    public void test() {
        System.out.println(translateNum(125214));
    }

    public int translateNum(int num) {
        String string = String.valueOf(num);
        // i = 0 的时候，不用判断,最大last肯定为 1 。移动i = 2的时候，还会为1。这样就移动3次了
        int pre = 0, post = 1, last = 1;
        for (int i = 1; i < string.length(); ++i) {
            pre = post;
            post = last;

            //截取 上一个字符串和当前字符串
            String str = string.substring(i - 1, i + 1);
            Integer value = Integer.valueOf(str);
            if (value <= 25 && value >= 10) {
                last = post + pre;
            }
        }
        return last;
    }

}
