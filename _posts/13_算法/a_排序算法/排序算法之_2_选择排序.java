package com.hlj.arith.a_排序算法;

import org.junit.Test;

import java.util.Arrays;


/**
作者：HealerJean
题目：
 首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置。
 再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
 重复第二步，直到所有元素均排序完毕。
解题思路：
*/
public class 排序算法之_2_选择排序 {


    @Test
    public void test(){
        int[] nums = {49, 38, 65, 97, 76, 13, 27, 50};
        选择排序(nums);
        System.out.println(Arrays.toString(nums));
    }


    public void 选择排序(int[] nums) {
        //从前往后比较，从0开始，是因为它要赋值给min,i一直到a.length 也就是最后一个还需要往前移动
        for (int i = 0; i < nums.length - 1; i++) {
            //首先默认第一个为最小值
            int idx = i;
            //是和tId比较 ，不需要自己跟自己比较，min初始给的i，所以j=i+1;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[idx] > nums[j]) {
                    idx = j;
                }
            }

            //每趟排序之后，idex的值都会不一样 ,而每次的min都是开始的i，所以当下的i和min进行替换
            if (idx != i) {
                int temp = nums[i];
                nums[i] = nums[idx];
                nums[idx] = temp;
            }
        }
    }



}
