package com.hlj.arith.a_排序算法;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author HealerJean
 * @date 2020/12/3  16:07.
 * @description
 */
public class TestMain {

    @Test
    public void test() {
        int nums[] = {49, 38, 65, 97, 76, 13, 27, 50};
        直接插入排序(nums);
        System.out.println(Arrays.toString(nums));
    }


    private void 直接插入排序(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int cur = nums[i];
            int j = i ;
            for (; j >= 0; j--) {
                if (nums[j] < nums[j-1]) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }else {
                    break;
                }
            }
        }
    }


}
