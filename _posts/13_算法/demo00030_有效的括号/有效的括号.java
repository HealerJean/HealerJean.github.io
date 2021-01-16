package com.hlj.arith.demo00030_有效的括号;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
作者：HealerJean
题目：有效的括号
 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串，判断字符串是否有效。
 有效字符串需满足：左括号必须用相同类型的右括号闭合。左括号必须以正确的顺序闭合。注意空字符串可被认为是有效字符串。
     示例 1:
         输入: "()"
         输出: true
     示例 2:
         输入: "()[]{}"
         输出: true
     示例 3:
         输入: "(]"
         输出: false
     示例 4:
         输入: "([)]"
         输出: false
     示例 5:
         输入: "{[]}"
         输出: true
 解题思路：借助栈存放数据，先进先出，栈里面的存放的数据只能为 ( { [
*/
public class 有效的括号 {

    @Test
    public void test() {
        System.out.println(isValid( "()"));
    }

    public boolean isValid(String str) {
        Map<Character, Character> map = new HashMap();
        map.put('(', ')');
        map.put('[', ']');
        map.put('{', '}');
        Stack<Character> stack = new Stack();
        for (int i = 0; i < str.length(); i++) {
            Character ele = str.charAt(i);
            //如果属于 map的key值则直接放入栈中
            if (map.containsKey(str.charAt(i))) {
                stack.push(ele);
            } else {
                //如果不属于 map的key，还能进来肯定有问题
                if (stack.isEmpty()) {
                    return false;
                } else if (map.get(stack.peek()).equals(ele)) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        if (stack.isEmpty()) {
            return true;
        }
        return false;
    }


    /**
     * 解法2。和上面原理相同，上面的方法是左括号入栈，下面则是右括号入栈
     */
    public boolean isValid2(String s) {
        Stack<Character> stack = new Stack();
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(')');
            } else if (c == '[') {
                stack.push(']');
            } else if (c == '{') {
                stack.push('}');
             //如果不等于栈顶元素则肯定错误，但是如果相等了就出栈
            } else if (stack.isEmpty() || c != stack.pop()) {
                return false;
            }
        }
        return stack.isEmpty();
    }
}
