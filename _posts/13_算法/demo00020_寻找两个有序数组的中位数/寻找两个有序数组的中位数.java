package com.hlj.arith.demo00020_寻找两个有序数组的中位数;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName 寻找两个有序数组的中位数
 * @date 2020/2/20  13:45.
 * @Description
 */

/**
 作者：HealerJean
 题目：寻找两个有序数组的中位数
 解题思路：看博客，有图文解释
 */
public class 寻找两个有序数组的中位数 {


    @Test
    public void test() {
        int[] A = new int[]{1, 3};
        int[] B = new int[]{2};

        System.out.println(findMedianSortedArrays(A, B));
    }


    public double findMedianSortedArrays(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        //如果数组 A 比较长，则交换 A、B 数组
        if (m > n) {
            return findMedianSortedArrays(B, A);
        }


        //增加i的方式使用折半查找,下面有i的边界判断，所以这里imax = m即可
        int iMin = 0, iMax = m ;

        while (iMin <= iMax) {
            //i 折半查找中间值
            int i = (iMin + iMax) / 2;
            int j = (m + n + 1) / 2 - i;

            // i 需要增大
            //数组 A 分割点相邻左边那个元素比数组 B 分割点相邻右边那个元素大，则应该将数组 A 分割点向右移，数组 B 分割点向左移
            //数组 A 分割点有向左移趋势，需检查左边界
            if (j != 0 && i != m && B[j - 1] > A[i]) {
                iMin = i + 1;
                // i 需要减小
                //数组 A 分割点相邻右边那个元素比数组 B 分割点相邻左边那个元素大，则应该将数组 A 分割点向左移，数组 B 分割点向右移
                //数组 A 分割点有向右移趋势，需检查右边界

            } else if (i != 0 && j != n && A[i - 1] > B[j]) {
                iMax = i - 1;


            } else { // 达到要求，并且将边界条件列出来单独考虑
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                // 奇数的话不需要考虑右半部分（因为奇数的话，左面自然就多了一个数字）
                if ((m + n) % 2 == 1) {
                    return maxLeft;
                }

                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(B[j], A[i]);
                }

                //如果是偶数的话返回结果
                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0;
    }


}
