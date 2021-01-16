package com.hlj.arith.demo00013_两个有序数组合并成同一个;

import org.junit.Test;

import java.util.Arrays;


/**
作者：HealerJean
题目：两个有序数组合并成一个数组
    数组A (9,8,7,6,5,4,2,1)
    数组B (8,5,4,3,4,6)
解题思路：
    新建一个数组C ，上面开头的两个数组，依次比较大小，大的放进来，9先进入，指针移动到8，然后8再和下面的进行比较，相同，分别移动进来，然后A数组指针移动饿到7，数组B指针移动到5，依次执行
 */
public class 合并两个有序数组_1 {

    @Test
    public void test() {
        int a[] = {7, 6, 5, 4, 3, 2, 1, 0};
        int b[] = {9, 9, 9, 9, 8, 5, 3, 2};
        int c[] = new int[a.length + b.length], i = 0, j = 0, n = 0;
        //保证两个数组同时遍历
        while (i < a.length && j < b.length) {
            if (a[i] > b[j]) {
                c[n] = a[i];
                i++;
            } else {
                c[n] = b[j];
                j++;
            }
            n++;
        }

        //上面有一方会提前结束
        while (i < a.length) {
            c[n] = a[i];
            i++;
            n++;
        }

        while (j < b.length) {
            c[n] = b[j];
            j++;
            n++;
        }

        System.out.println(Arrays.toString(c));
    }


}
