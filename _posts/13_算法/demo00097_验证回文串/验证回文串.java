package com.hlj.arith.demo00097_验证回文串;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


/**
作者：HealerJean
题目：验证回文串
 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
     说明：本题中，我们将空字符串定义为有效的回文串。
     示例 1:
         输入: "A man, a plan, a canal: Panama"
         输出: true
     示例 2:
         输入: "race a car"
         输出: false
解题思路：
*/
@Slf4j
public class 验证回文串 {

    @Test
    public void test(){
        System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
    }


    @Test
    public void characterTest(){
        char a = 'a' ;
        System.out.println(Character.isDigit(a)); //是否是数字
        System.out.println(Character.isLetter(a)); //是否是子母
        System.out.println(Character.isLetterOrDigit(a)); //是否是子母或者数字

        System.out.println(Character.isUpperCase(a)); // 是否大写
        System.out.println(Character.isLowerCase(a)); //是否小写

        System.out.println(Character.toLowerCase(1)); //1
        System.out.println(Character.toLowerCase('A')); //a
    }


    public boolean isPalindrome(String s) {
        if (s.length() == 0) {
            return true;
        }

        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            char leftVal = s.charAt(left);
            if (!Character.isLetterOrDigit(leftVal)) {
                left++;
                continue;
            }
            char rightVal = s.charAt(right);
            if (!Character.isLetterOrDigit(rightVal)) {
                right--;
                continue;
            }

            if (Character.toLowerCase(leftVal) != Character.toLowerCase(rightVal)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

}
