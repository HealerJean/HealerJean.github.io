package com.hlj.arith.demo00110_两个数组的交集;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
作者：HealerJean
题目：
 给定两个数组，编写一个函数来计算它们的交集。
     示例 1：
         输入：nums1 = [1,2,2,1], nums2 = [2,2]
         输出：[2]
     示例 2：
         输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
         输出：[9,4]
解题思路：
*/
public class 两个数组的交集_1 {

    @Test
    public void test() {
        int[] nums1 = {4, 9, 5};
        int[] nums2 = {9, 4, 9, 8, 4};

        System.out.println(Arrays.toString(intersection(nums1, nums2)));
    }


    public int[] intersection(int[] nums1, int[] nums2) {
        List<Integer> list = Arrays.stream(nums1).mapToObj(Integer::new).collect(Collectors.toList());
        Set<Integer> set = new HashSet<>();
        for (int num : nums2) {
            if (list.contains(num)) {
                set.add(num);
            }
        }
        return set.stream().mapToInt(Integer::intValue).toArray();
    }
}
