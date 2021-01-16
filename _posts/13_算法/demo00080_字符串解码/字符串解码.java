package com.hlj.arith.demo00080_字符串解码;

import org.junit.Test;

import java.util.*;

/**
作者：HealerJean
题目：字符串解码

 给定一个经过编码的字符串，返回它解码后的字符串。
 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
 示例:
 s = "3[a]2[bc]", 返回 "aaabcbc".
 s = "3[a2[c]]", 返回 "accaccacc".
 s = "2[abc]3[cd]ef", 返回 "abcabccdcdcdef".

 以及 "3[a2[c]]" accaccacc
 解题思路：利用栈的特性，一直进入，直到遇到 ]  ，然后找出栈中的第一个 [
*/
public class 字符串解码 {

    @Test
    public void test(){
        System.out.println(decodeString("3[a2[c]]"));
    }

    public String decodeString(String s) {
        List<Character> numberList = Arrays.asList('0', '1', '2','3','4','5','6','7','8','9');
        Stack<Character> stack = new Stack();
        for (int i = 0 ; i < s.length(); i++){
            StringBuilder temStr = new StringBuilder();
            StringBuilder temNumStr = new StringBuilder();
            if (s.charAt(i) == ']'){
                //将[]内的东西取出
                while (stack.peek() != '['){
                    temStr.append(stack.pop());
                }
                // 出 [
                stack.pop();

                //将 [ 前面的数字取出来，一定要判断空哦，因为有可能是第一个
                while (!stack.isEmpty() && numberList.contains(stack.peek())){
                    temNumStr.append(stack.pop());
                }
                // 栈中的顺序是反的，所以要倒叙
                int num = Integer.valueOf(temNumStr.reverse().toString());
                int count = 0 ;
                StringBuilder temResStr = new StringBuilder();
                while (count < num){
                    temResStr.append(temStr);
                    count++;
                }
                //新的对象构造完成，记得再放回栈中
                int tempResStrLen = temResStr.toString().length();
                for (int j = tempResStrLen-1 ; j >= 0; j--){
                    stack.push(temResStr.toString().charAt(j));
                }
            }else {
                stack.push(s.charAt(i));
            }
        }

        StringBuilder res = new StringBuilder();
        while (!stack.isEmpty()){
            res.append(stack.pop());
        }
        //注意反转哦，栈里面都是倒叙输出的
        return res.reverse().toString();
    }
}
