package com.hlj.arith.demo00037_最长有效括号;

import org.junit.Test;

import java.util.Stack;

/**
 作者：HealerJean
 题目：最长有效括号
    给定一个只包含 '(' 和 ')' 的字符串，找出最长的包含有效括号的子串的长度。
     示例 1:
         输入: "(()"
         输出: 2
     示例 2:
        输入: ")()())"
        输出: 4
 解题思路：有效括号一次放在栈中，然后从中取出来。再栈的底部保留一个 ) 或者 -1,用来截取有效括号的长度
 */
public class 最长有效括号 {

    @Test
    public void test() {
        System.out.println(longestValidParentheses("()"));
    }

    public int longestValidParentheses(String s) {
        int max = 0;
        Stack<Integer> stack = new Stack<>();
        //占位置
        stack.push(-1);
        for (int i = 0; i < s.length(); i++) {
            //如果是 ( 放心入栈
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                //正常情况下，出栈的是( ，但是这个时候栈里面是 -1 或者是 ) 那么取出来栈就变成空了，所以如果是空的情况，还会把它放进去
                stack.pop();
                if (stack.empty()) {
                    //不能让栈变成空，因为有值的情况才能进行截取
                    stack.push(i);
                } else {
                    //截取长度
                    max = Math.max(max, i - stack.peek());
                }
            }
        }
        return max;
    }




}
