package com.hlj.arith.demo00176_比较含退格的字符串;

import org.junit.Test;

import java.util.Stack;

/**
作者：HealerJean
题目：比较含退格的字符串
 给定 S 和 T 两个字符串，当它们分别被输入到空白的文本编辑器后，判断二者是否相等，并返回结果。 # 代表退格字符。
 注意：如果对空文本输入退格字符，文本继续为空。
     示例 1：
         输入：S = "ab#c", T = "ad#c"
         输出：true
         解释：S 和 T 都会变成 “ac”。
     示例 2：
         输入：S = "ab##", T = "c#d#"
         输出：true
         解释：S 和 T 都会变成 “”。
     示例 3：
         输入：S = "a##c", T = "#a#c"
         输出：true
         解释：S 和 T 都会变成 “c”。
     示例 4：
         输入：S = "a#c", T = "b"
         输出：false
         解释：S 会变成 “c”，但 T 仍然是 “b”。
解题思路：
*/
public class 比较含退格的字符串 {


    @Test
    public void test() {
        System.out.println(backspaceCompare("a##c", "#a#c"));
        System.out.println(backspaceCompare2("a##c", "#a#c"));
    }

    public boolean backspaceCompare(String S, String T) {
        return newStr(S).equals(newStr(T));
    }

    /**
     * 方法1：使用指针
     */
    public String newStr(String str){
        int count = 0 ;
        int i = str.length()-1;
        StringBuilder builder = new StringBuilder();
        while (i >= 0){
            if (str.charAt(i) == '#'){
                count++;
            }else if (count > 0){
                count--;
            }else {
                builder.append(str.charAt(i));
            }
            i--;
        }
        return builder.reverse().toString();
    }


    /**
     * 2、方法2，一个一个比较
     */
    public boolean backspaceCompare2(String S, String T) {
        int i = S.length() - 1, j = T.length() - 1;
        int skipS = 0, skipT = 0;

        while (i >= 0 || j >= 0) {

            //走到不退格的时候的字符
            while (i >= 0) {
                if (S.charAt(i) == '#') {
                    skipS++;
                    i--;
                } else if (skipS > 0) {
                    skipS--;
                    i--;
                } else {
                    break;
                }
            }
            while (j >= 0) {
                if (T.charAt(j) == '#') {
                    skipT++;
                    j--;
                } else if (skipT > 0) {
                    skipT--;
                    j--;
                } else {
                    break;
                }
            }


            if (i >= 0 && j >= 0) {
                //比较当前的是否相等
                if (S.charAt(i) != T.charAt(j)) {
                    return false;
                }
            } else {
                //到了此处肯定有i < 0 或者 j < 0的结果
                if (i >= 0 || j >= 0) {
                    return false;
                }
            }

            i--;
            j--;
        }
        return true;
    }

}
