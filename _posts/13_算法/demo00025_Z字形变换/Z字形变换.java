package com.hlj.arith.demo00025_Z字形变换;

import org.junit.Test;

/**
作者：HealerJean
题目：Z字形变换
解题思路：画图找规律

*/
public class Z字形变换 {

    @Test
    public void test(){

        System.out.println(z("LEETCODEISHIRING", 3));
    }

    public String z(String s, int numRows){
        int length = s.length();
        if (length <= numRows || numRows == 1){
            return s ;
        }

        //先打印第一行的数据
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(s.charAt(0)) ;
        int initWidth = (numRows -1 ) * 2 ;
        int j = initWidth ;
        for ( ; j < length; j += initWidth){
            stringBuilder.append(s.charAt(j)) ;
        }


        //从第二行开始
        for (int i = 1; i < numRows ; i++) {
            stringBuilder.append(s.charAt(i)) ;
            int width = initWidth - (i * 2);
            if (width != 0) {
                j = i + width ;
                for ( ; j < length; j +=  width){
                    stringBuilder.append(s.charAt(j)) ;
                    width = initWidth - width ;
                }
            } else {
                j = i + initWidth ;
                for ( ; j < length; j += initWidth){
                    stringBuilder.append(s.charAt(j)) ;
                }
            }
        }

        return stringBuilder.toString();
    }
}
