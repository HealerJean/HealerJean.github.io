package com.hlj.arith.demo00013_两个有序数组合并成同一个;

import org.junit.Test;

import java.util.Arrays;

public class 合并两个有序数组_2 {

    @Test
    public void test() {
        int nums1[] = {2, 3, 4, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int nums2[] = {1, 2, 3};
        merge(nums1, 4, nums2, 3);
        System.out.println(Arrays.toString(nums1));
    }

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int x = (m--) + (n--) - 1 ;
        while(m >= 0 && n >= 0){
            nums1[x--] = nums1[m] > nums2[n] ? nums1[m--]  : nums2[n--];
        }
        while (n >= 0) {
            nums1[x--] = nums2[n--];
        }
    }
}
