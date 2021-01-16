package com.hlj.arith.demo00038_两数相除;

import org.junit.Test;

/**
作者：HealerJean
题目：两数相除
    给定两个整数，被除数 dividend 和除数 divisor。将两数相除，
    要求不使用乘法、除法和 mod 运算符。返回被除数 dividend 除以除数 divisor 得到的商。
        示例 1:
            输入: dividend = 10, divisor = 3
            输出: 3
        示例 2:
            输入: dividend = 7, divisor = -3
            输出: -2
解题思路：

 假设我们的环境只能存储 32 位有符号整数，其数值范围是 [−2^31,  23^1 − 1]。本题中，如果除法结果溢出，则返回 2^31 − 1。
 最小负数：Integer.MIN_VALUE =  -2147483648
 最大正数：Integer.MAX_VALUE =  -2147483647

 */
public class 两数相除 {


    /**

     */
    @Test
    public void test() {
        System.out.println(divide( 1, 1));
    }


    /**
     * b是除数 a是被除数 b/a = result
     */
    public int divide(int a, int b) {

        //第一个极端情况，必须满足同时成立
        //  a = -2^31, b = -1, a/b = 2^31 内存溢出（Integer.MIN_VALUE/-1  = 2147483648 内存溢出），返回 Integer.MAX_VALUE
        if (a == Integer.MIN_VALUE && b == -1) {
            return Integer.MAX_VALUE;
        }

        //负数最大值为被除数的时候。讨论极端情况
        // a != -2^31, b = -2^31, a/b = 0 ，就没有任何数可以处以最小的负数，因为负数的绝对值太大了，所以返回 0
        if (b == Integer.MIN_VALUE) {
            // a = -2^31, b = -2^31, a/b = 1， 下面  while (b <=  (a >> j)){ 除了
            //除了它自己能除以自己，没人能除以它
            if (a == Integer.MIN_VALUE) {
                return 1;
            }
            return 0;
        }

        //当a是最大的负数的时候，绝对值内存就溢出了，
        // 所以想办法让他不溢出，那就是让他变成正数的时候数字变小。加上正数，减去小数 。最后结果计算完事了，在加或者减回来
        // 比如 -7 / 3 =>  7-3 = 4  4/3 = 1 =>   -1 - 1 = -2
        // 比如  7 / 3 =>  7-3 = 4  4/3 = 1 =>    1 + 1 =  2
        int buwei = 0;
        if (a == Integer.MIN_VALUE) {
            if (b > 0) {
                a = a + b;
                buwei = -1;
            } else {
                a = a - b;
                buwei = 1;
            }
        }


        //false 表示结果为负数，true表示结果为正数
        boolean flag = false;
        if ((b > 0 && a > 0) || (b < 0 && a < 0)) {
            flag = true;
        }
        //将 a 和 b 都变成正数
        if (a < 0) {
            a = -a;
        }
        if (b < 0) {
            b = -b;
        }


        int result = 0 ;

        //从我们的解决思路逆推
        while (a >= b) {
            // 左移个数
            int j = 1;
            //你像思维，a不断右移（也就是不断除以2） 如果比a大的话，就继续执行，直到比b小
            while (b <=  (a >> j)){
                j++;
            }
            j = j -1 ;
            a = a - (b << j);
            //结果相加
            result = result +  (1 << j) ;
        }

        //下面这种情况是一步步增大 值，很容易溢出，数字比较小还行，数字一大，就完蛋了
        // while (a >= b) {
        //     // 左移个数
        //     int j = 1;
        //     while ((b << j) < a) {
        //         j++;
        //     }
        //     //j 在上面多加了1，真正的应该减去1
        //     j = j - 1;
        //     a = a - (b << j);
        //     //结果相加
        //     result = result +  (1 << j) ;
        // }
        return (flag ? result : -result) + buwei;
    }

}
