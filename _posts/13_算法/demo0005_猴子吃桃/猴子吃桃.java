package com.hlj.arith.demo0005_猴子吃桃;


import org.junit.Test;

import java.util.Scanner;

/**
作者：HealerJean
题目：猴子吃桃
    某天，一只猴子摘了一堆桃子，它每天吃掉其中的一半再多吃一个，第二天吃剩余的一半再多吃一个······，到了第N天只剩下一个桃子，问在第一天时摘了多少桃子？
解题思路：  通过上面的我们可以知道，这道题是可以逆推的，所以很明显可以使用递归
    eat(h)  = (eat(h - 1) + 1) * 2
 */
public class 猴子吃桃 {

    @Test
    public void test() {
        System.out.println(eat(2));
    }

    public static int eat(int h) {
        if (h == 1) {
            return 1;
        } else {
            return (eat(h - 1) + 1) * 2;
        }
    }
}
