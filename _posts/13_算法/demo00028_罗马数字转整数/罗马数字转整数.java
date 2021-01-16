package com.hlj.arith.demo00028_罗马数字转整数;

import org.junit.Test;

/**
作者：HealerJean
题目：罗马数字转整数
解题思路：接27
*/
public class 罗马数字转整数 {

    @Test
    public void test(){
        System.out.println(romanToInt("MCMXCIV"));
    }

    public int romanToInt(String s) {
        int[] nums = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romans = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int length = nums.length;
        int index = 0;
        int num = 0 ;
        while (index < length) {
            // 这里是等号，这样就保证了那种特殊的数字
            while (s.startsWith(romans[index])) {
                 //截取除匹配字符串后面的
                 s =  s.substring(s.indexOf(romans[index])+ romans[index].length());
                num = num + nums[index] ;
            }
            index++;
        }
        return  num;

    }
}
