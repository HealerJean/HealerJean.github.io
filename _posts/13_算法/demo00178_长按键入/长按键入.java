package com.hlj.arith.demo00178_长按键入;

import org.junit.Test;

/**
作者：HealerJean
题目：
 你的朋友正在使用键盘输入他的名字 name。偶尔，在键入字符 c 时，按键可能会被长按，而字符可能被输入 1 次或多次。
 你将会检查键盘输入的字符 typed。如果它对应的可能是你的朋友的名字（其中一些字符可能被长按），那么就返回 True。
 示例 1：
     输入：name = "alex", typed = "aaleex"
     输出：true
     解释：'alex' 中的 'a' 和 'e' 被长按。
 示例 2：
     输入：name = "saeed", typed = "ssaaedd"
     输出：false
     解释：'e' 一定需要被键入两次，但在 typed 的输出中不是这样。
 示例 3：
     输入：name = "leelee", typed = "lleeelee"
     输出：true
 示例 4：
     输入：name = "laiden", typed = "laiden"
     输出：true
     解释：长按名字中的字符并不是必要的。
解题思路：
*/
public class 长按键入 {


    @Test
    public void test() {
        // System.out.println(isLongPressedName("alex", "aaleex"));
        // System.out.println(isLongPressedName("leelee", "lleeelee"));
        // System.out.println(isLongPressedName("laiden", "laiden"));
        // System.out.println(isLongPressedName("saeed", "ssaaedd"));
        // System.out.println(isLongPressedName("alex", "alexxr"));
        System.out.println(isLongPressedName("vtkgn", "vttkgnn"));
    }

    /**
     * 算法1：我自己的
     */
    public boolean isLongPressedName(String name, String typed) {
        char pre = '0';
        int i = 0;
        int j = 0;
        while (i < name.length() && j < typed.length()) {
            //如果字符匹配
            if (name.charAt(i) == typed.charAt(j)) {
                pre = typed.charAt(j);
                i++;
                j++;
            } else if (typed.charAt(j) == pre) {
                j++;
            } else {
                return false;
            }
        }


        if (i == name.length()){
            while (j < typed.length()){
                if (typed.charAt(j) == pre) {
                    j++;
                }else {
                    return false;
                }
            }
        }
        return i == name.length() && j == typed.length();
    }


    /**
     * 算法2：官方
     */
    public boolean isLongPressedName2(String name, String typed) {
        int i = 0, j = 0;
        char pre = '0';
        //必须遍历完j
        while (j < typed.length()) {
            //i <  name.length() 才有意义
            if (i < name.length() && name.charAt(i) == typed.charAt(j)) {
                pre =  typed.charAt(j);
                i++;
                j++;
            } else if (typed.charAt(j) == pre) {
                j++;
            } else {
                return false;
            }
        }
        return i == name.length();
    }


}
