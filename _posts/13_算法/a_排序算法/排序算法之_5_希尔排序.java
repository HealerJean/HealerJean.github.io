package com.hlj.arith.a_排序算法;

import org.junit.Test;

import java.util.Arrays;

/**
 * 作者：HealerJean
 * 题目：
 * 解题思路：
 */
public class 排序算法之_5_希尔排序 {

    @Test
    public void test() {
        int nums[] = {49, 38, 65, 97, 76, 13, 27, 50};
        希尔排序(nums);
        System.out.println(Arrays.toString(nums));
    }

    public void 希尔排序(int nums[]) {
        //希尔排序增量，//被分成4组 ，也即是第1个和第5个进行比较 ，低2个和低6个比较
        int incr = nums.length / 2;
        //当增量为0的时候排序完成
        while (incr > 0) {
            //以为是从前往后第一个数字开始比较，所以初始化i=0 ，插入排序是从后往前比较, 小于a.length 表示的是有坑呢到最后分成最后一组的时候会 相互挨着的笔记，所以一定要到结尾
            for (int i = 0; i < nums.length - 1; i++) {
                // 这里的每一趟相当于是一次插入排序的排序算法，不同的是，这里是从前往后
                for (int j = i; j < nums.length - incr; j = j + incr) {
                    if (nums[j] > nums[j + incr]) {
                        int temp = nums[i];
                        nums[i] = nums[j];
                        nums[j] = temp;
                    }
                }
            }
            incr = incr / 2;
        }
    }

}
