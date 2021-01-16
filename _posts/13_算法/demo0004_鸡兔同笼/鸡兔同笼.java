package com.hlj.arith.demo0004_鸡兔同笼;


import org.junit.Test;

/**
作者：HealerJean
题目：鸡兔同笼 和百元买百鸡一个意思
    1只鸡有1个头，2只脚，1只兔子有1个头，4只脚，若已知头的数量和脚的数量，求鸡和兔子各有多少？
解题思路： 输入 头x 脚y ,输出鸡a 兔子b
    a + b = x
    a * 2 + b * 4 = y
 */
public class 鸡兔同笼 {

    @Test
    public void start() {
        int x = 2;
        int y = 6;
        int a, b;
        for (a = 0; a <= x; a++) {
            b = x - a;
            if (a * 2 + b * 4 == y) {
                System.out.println("鸡的数量为：" + a + "，兔的数量为：" + b);
                return;
            }
        }
        System.out.println("不存在该组合");
    }
}



