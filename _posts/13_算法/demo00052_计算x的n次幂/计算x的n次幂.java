package com.hlj.arith.demo00052_计算x的n次幂;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName 计算x的n次幂
 * @date 2020/4/22  16:52.
 * @Description
 */
/**
作者：HealerJean
题目：
解题思路： 快速幂算法
 将n看成二进制数，比如n为11，3^11==3^10 *  3^1，也就是从低位往高位不断循环右移一位且当前x值为x*=x，最低位遇到1也就是当前为奇数res乘当前x即res*=x

 */
public class 计算x的n次幂 {

    @Test
    public void test(){

        System.out.println(myPow(2.0000, 6));
    }

    /**
     * 暴力法
     */
    public double myPow1(double x, int n) {

        if (n == 0 ){
            return 1 ;
        }

        double res = 1 ;
        if (n < 0 ){
           x = 1/x ;
            n = Math.abs(n);
        }

        while (n > 0) {
            res = res * x;
            n--;
        }
        return res;
    }


    /**
     * 假如我们现在知道了a^5的值，那么a的10次方只需要用 a的5次方乘以a的5次方即可。同样的，求a的5次方的过程 和 求a的10次方是同样的过程。
     *  (a的5次方 = a的2次方 * a的2次方 * a) ，指数n是奇数的话，那么 （a^n = a^(n/2) * a^(n/2) * a ）。
     */
    public double myPow(double x, int n) {
        boolean flag = false;
        if (n >= 0) {
            flag = true;
        }
        double res = 1;
        //i!= 0  保证不论是正数还是负数 都到了0的位置截止
        for (int i = n; i != 0; i /= 2) {
            //如果是奇数的话，要和自己相乘
            if (i % 2 != 0) {
                res *= x;
            }
            x *= x;
        }
        return flag ? res : 1 / res;
    }

}
