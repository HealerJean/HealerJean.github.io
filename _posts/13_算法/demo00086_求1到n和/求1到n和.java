package com.hlj.arith.demo00086_求1到n和;

import org.junit.Test;

/**
作者：HealerJean
题目：求1+2+…+n
 求 1+2+...+n ，要求不能使用乘除法、for、while、if、else、switch、case等关键字及条件判断语句（A?B:C）。
 限制：1 <= n <= 10000
     示例 1：
         输入: n = 3
         输出: 6
     示例 2：
         输入: n = 9
         输出: 45
解题思路：
*/
public class 求1到n和 {

    @Test
    public void test(){
        // System.out.println(recursion(2));
        // System.out.println(whileT(2));
        System.out.println(sumNums(2));

    }

    /***
     * 利用 && 特性，如果前部分错误了了，则不会执行后面的部分
     * 事实上，还是使用了递归
     */
    public int sumNums(int n) {
        boolean flag = n > 0 && (n += sumNums(n - 1)) < 0;
        return n;
    }

    /**
     * 递归
     */
    public int recursion(int n) {
        if (n == 1){
            return 1;
        }
        return recursion(n-1) + n ;
    }

    /**
     * for循环
     */
    public int whileT(int n) {
        int sum = 0 ;
        for (int i = 1 ; i <= n ; i++){
            sum = sum + i ;
        }
        return sum;
    }


}
