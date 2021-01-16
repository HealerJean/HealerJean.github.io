package com.hlj.arith.demo00133_字符串相乘;


import org.junit.Test;

/**
作者：HealerJean
题目：
 给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。
 说明：
     num1 和 num2 的长度小于110。
     num1 和 num2 只包含数字 0-9。
     num1 和 num2 均不以零开头，除非是数字 0 本身。
     不能使用任何标准库的大数类型（比如 BigInteger）或直接将输入转换为整数来处理。
 示例 1:
     输入: num1 = "2", num2 = "3"
     输出: "6"
 示例 2:
     输入: num1 = "123", num2 = "456"
     输出: "56088"

解题思路：
 关键信息：num1 x num2 结果 res 最大总位数为 M+N
    以3位数 * 2位数为例 =>
        长度最长不会超过1000(4位数)*100(3位数) = 100000(6位数) => 推理得出 最大位数肯定是5位数 也就是时 3 + 2 = 5
        长度最端不会小于100(3位数)*10(2位数)   = 1000(4位数)   => 推理得出 最小位数肯定是4位数 也就是时 3 + 2 - 1 = 4 （这种情况也就是 首位为0）


 */
public class 字符串相乘 {


    @Test
    public void test(){
        System.out.println(multiply("45", "123"));
        System.out.println(multiply("100", "10"));

    }

    public String multiply(String num1, String num2) {
        if (num1.equals("0") || num2.equals("0")) {
            return "0";
        }
        int[] res = new int[num1.length() + num2.length()];
        for (int i = num1.length() - 1; i >= 0; i--) {
            int a = num1.charAt(i) - '0';
            for (int j = num2.length() - 1; j >= 0; j--) {
                int b = num2.charAt(j) - '0';
                //原来这个位置上的数 + a * b
                int sum = (res[i + j + 1] + a * b);
                res[i + j + 1] = sum % 10;
                //向上进1位
                res[i + j] += sum / 10;
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < res.length; i++) {
            // 出现首位为0的情况则跳过
            if (i == 0 && res[i] == 0) {
                continue;
            }
            result.append(res[i]);
        }
        return result.toString();
    }
}
