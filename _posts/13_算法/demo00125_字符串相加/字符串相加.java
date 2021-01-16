package com.hlj.arith.demo00125_字符串相加;

import org.junit.Test;

/**
作者：HealerJean
题目：
 给定两个字符串形式的非负整数 num1 和num2 ，计算它们的和。
 注意：
     num1 和num2 的长度都小于 5100.
     num1 和num2 都只包含数字 0-9.
     num1 和num2 都不包含任何前导零。
     你不能使用任何內建 BigInteger 库， 也不能直接将输入的字符串转换为整数形式。
解题思路：类似于二进制之和
*/
public class 字符串相加 {

    @Test
    public void test() {
        System.out.println(addStrings("9", "99"));
    }


    public String addStrings(String num1, String num2) {
        StringBuilder append = new StringBuilder();

        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int t = 0;
        while (j >= 0 || i >= 0 || t > 0) {
            int pre = i < 0 ? 0 : num1.charAt(i--) - '0';
            int post = j < 0 ? 0 : num2.charAt(j--) - '0';
            int sum = pre + post + t;
            t = sum / 10;
            append.append(sum % 10);
        }
        return append.reverse().toString();
    }
}
