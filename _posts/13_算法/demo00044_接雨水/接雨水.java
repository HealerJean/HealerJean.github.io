package com.hlj.arith.demo00044_接雨水;

/**
 * @author HealerJean
 * @ClassName 接雨水
 * @date 2020/4/7  20:05.
 * @Description
 */

import org.junit.Test;


/**
作者：HealerJean
题目：
 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
解题思路：其实仔细想一下，
    如果有2根柱子，那肯定是以最短的柱子为主
    如果有3根柱子，肯定是最小的两根柱子为主
    如果有4根柱子，肯定要考虑挨着的两根柱子，分别是多少高度，。然后选择最长的那根主，依次向两边扩展。这样就可以使用双指针了

 */
public class 接雨水 {


    @Test
    public void test(){
        int [] height = {0,1,0,2,1,0,1,3,2,1,2,1};
        // int [] height = {5,4,1,2};
        System.out.println(trap(height));
    }

    public int trap(int[] height) {
        if (height.length < 3) {
            return 0;
        }
        //水的容量
        int water = 0;

        int left = 0, right = height.length - 1;
        int maxLeft = 0,   maxRight = 0;


        //左指针永远不会超过右指针
        while (left < right) {
            //如果座指针的高度小于右指针的高度，则我们才可以计算左指针所移动带来的水池子。否则要计算右面的指针的水池子，
            //你想啊。加入有两根柱子，如果左面的指针比右面的大了，是不是代表着。我们只能以右面的住柱子为主。反之以左面的柱子为主
            if (height[left] < height[right]) {
                //如果left指针往右走，如果柱子越来越高。肯定是左部分蓄水能力越来越高， 所以获取最长的左柱子
                if (height[left] > maxLeft) {
                    maxLeft = height[left];
                } else {
                    //当left指针往右走，柱子比最长的柱子小的时候，肯定要蓄水了呀。具体当前这根柱子，蓄水值就等于最长的柱子减去当前的柱子。
                    water += maxLeft - height[left];
                }

                //左指针右移
                left++;

            } else {
                if (height[right] > maxRight) {
                    maxRight = height[right];
                } else {
                    water += maxRight - height[right];
                }

                right--;
            }
        }
        return water;
    }


}
