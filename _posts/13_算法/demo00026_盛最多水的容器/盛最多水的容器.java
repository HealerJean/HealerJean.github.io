package com.hlj.arith.demo00026_盛最多水的容器;


import org.junit.Test;

/**
 作者：HealerJean
 题目：盛最多水的容器
 解题思路：短移动 看代码和博客
 */
public class 盛最多水的容器 {


    @Test
    public void test(){
        int[] height = new int[]{1,8,6,2,5,4,8,3,7};
        System.out.println( maxArea(height));
    }


    public int maxArea(int[] height) {
        //指针
        int i = 0 ,j = height.length -1 ;
        //j和i之间的长度
        int length = 0 ;
        int max = 0 ;
        int minHeight = 0 ;
        while (i < j){
            length = j - i ;
            if (height[i] < height[j]){
                minHeight = height[i] ;
                i++ ;
            }else {
                minHeight = height[j];
                j-- ;
            }
            max = Math.max(max, length * minHeight);
        }
        return max ;
    }

}
