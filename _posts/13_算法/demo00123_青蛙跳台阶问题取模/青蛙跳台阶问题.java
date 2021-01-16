package com.hlj.arith.demo00123_青蛙跳台阶问题取模;

import org.junit.Test;

/**
作者：HealerJean
题目：
解题思路：
*/
public class 青蛙跳台阶问题 {
    @Test
    public void test() {
        System.out.println(numWays(7));
        System.out.println(8 % 1000000007);
    }

    public int numWays(int n) {
        if (n == 1 || n == 0){
            return  1;
        }
        if (n == 2){
            return  2;
        }
        int pre = 1;
        int post = 2;
        int last = 0 ;
        for (int i = 3; i <= n; i++) {
            last = (pre + post) % 1000000007;
            pre = post;
            post = last;
        }
        return last;
    }
}
