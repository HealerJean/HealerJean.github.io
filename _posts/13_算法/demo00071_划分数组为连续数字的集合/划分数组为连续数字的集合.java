package com.hlj.arith.demo00071_划分数组为连续数字的集合;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 作者：HealerJean
 * 题目：
 * 给你一个整数数组 nums 和一个正整数 k，请你判断是否可以把这个数组划分成一些由 k 个连续数字组成的集合。
 * 如果可以，请返回 True；否则，返回 False。
 * 解题思路：
 */
public class 划分数组为连续数字的集合 {


    @Test
    public void test() {
        int[] nums = {1, 2, 3, 4};
        int k = 3;
        System.out.println(isPossibleDivide(nums, k));
    }

    public boolean isPossibleDivide(int[] nums, int k) {

        //数组先排序
        Arrays.sort(nums);
        int allCount = 0;
        //count 每次数组中的个数
        int count = 0;
        //pre ：每次比较的数
        int pre = 0;
        //是否有第一个 pre
        boolean firstFlag = false;
        //use 是否已经被使用过
        boolean[] use = new boolean[nums.length];

        //每次while就是获取一个数组的集合，当allCount达到数组的长度的时候截止
        while (true) {
            if (allCount == nums.length) {
                //当 1，2，3，4, k = 3 的时候 ，当第一轮走完，开始走4 这个数字的时候，就不可以走下一轮了，直接出去，此时count为 1
                if (count == k) {
                    return true;
                } else {
                    return false;
                }
            }


            for (int i = 0; i < nums.length; i++) {
                //有第一个pre，则一直continue，一直到完成
                //获取第一个 pre ,并且当前位置没有被使用过，否则会一直contin
                if (firstFlag == false) {
                    //当当前 i 没有使用过，则找到了pre
                    if (use[i] == false) {
                        pre = nums[i];
                        firstFlag = true;
                        use[i] = true;
                        count = 1;
                        //每次获取到数组中的结果集都要  + 1
                        allCount++;
                    }
                    continue;
                }

                //如果使用过 || 相等的时候。则继续走
                if (use[i] || nums[i] == pre) {
                    continue;
                }
                //当当前位置比 pre大1的时候，获取到结果，否则就代表着不存在连续的。肯定返回false
                if (pre + 1 == nums[i]) {
                    use[i] = true;
                    pre = nums[i];
                    allCount++;
                    count++;
                    //如果count == 4 ，表示一轮完成。开始准备下一轮
                    if (count == k) {
                        firstFlag = false;
                        break;
                    }
                } else {
                    return false;
                }
            }
        }

    }


}
