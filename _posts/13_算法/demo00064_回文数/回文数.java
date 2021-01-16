package com.hlj.arith.demo00064_回文数;

import org.junit.Test;

/**
作者：HealerJean
题目：回文数
    判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 示例 1:
     输入: 121
     输出: true
 示例 2:
     输入: -121
     输出: false
     解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
 示例 3:
     输入: 10
     输出: false
     解释: 从右向左读, 为 01 。因此它不是一个回文数。
 解题思路：
     就是一个整数反转，反转之后比较是否相等，如果是负数则直接输出即可
 */
public class 回文数 {

    @Test
    public void test(){
        System.out.println(isPalindrome(121));
    }

    public boolean isPalindrome(int x) {
        if (x < 0 ){
            return false;
        }

        int pop = 0 ;
        int res  = 0 ;
        int num = x ;
        while (x !=0 ){
            pop = x % 10 ;
            res = res * 10 + pop ;
            x = x / 10 ;
        }

        if (res == num){
             return true;
        }
        return false ;
    }
}
