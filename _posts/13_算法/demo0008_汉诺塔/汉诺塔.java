package com.hlj.arith.demo0008_汉诺塔;

/**
作者：HealerJean
题目：汉诺塔
    汉诺塔(Tower of Hanoi)源于印度传说中，大梵天创造世界时造了三根金钢石柱子，
    其中一根柱子自底向上叠着64片黄金圆盘。大梵天命令婆罗门把圆盘从下面开始按大小顺序重新摆放在另一根柱子上。
    并且规定，在小圆盘上不能放大圆盘，在三根柱子之间一次只能移动一个圆盘。在进行转移操作时，
    都必须确保大盘在小盘下面，且每次只能移动一个圆盘，最终c柱上有所有的盘子且也是从上到下按从小到大的顺序。
解题思路：
    有两个盘子的话把1号盘先移到b柱，在把2号盘移到c柱，最后把b柱上的1号盘移到c柱就行了，
    这个时候，想象将商品的63个盘子看做一个整体，最下面的1个盘子看做一个整体，也是这样解决的。
    那么接下来这63个盘子，再看做一个整体，依次下去，是不是就成功了，这样相当于是逆推回去了 。这样的话，我们就可以使用递归算法了
 */
public class 汉诺塔 {

    public static void method(int n, char A, char B, char C) {

        //如果只有一个盘子（此时盘子只在A上），则直接移动到C上
        if (n == 1) {
            move(A, C);
        } else {
            //将n-1个盘子由A经过C移动到B
            method(n - 1, A, C, B);

            //中间的一步是把最大的一个盘子由A移到C上去；
            move(A, C);

            //剩下的n-1盘子，由B经过A移动到C
            method(n - 1, B, A, C) ;
        }
    }
    public static int count  = 0 ;
    private static void move(char A, char C) {//执行最大盘子n的从A-C的移动
        System.out.println("第"+ (++count)+"次移动:" + A + "--->" + C);
    }

    public static void main(String[] args) {
        System.out.println("移动汉诺塔的步骤：");
        method(2, 'a', 'b', 'c');
    }
}
