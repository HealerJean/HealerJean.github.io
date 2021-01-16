package com.hlj.arith.demo00110_两个数组的交集;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
作者：HealerJean
题目：两个数组的交集
 给定两个数组，编写一个函数来计算它们的交集。
     示例 1：
         输入：nums1 = [1,2,2,1], nums2 = [2,2]
         输出：[2,2]
     示例 2:
         输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
         输出：[4,9]
解题思路：
*/
public class 两个数组的交集_2 {

    @Test
    public void test() {
        int[] nums1 = {4};
        int[] nums2 = {9};

        System.out.println(Arrays.toString( intersect(nums1, nums2)));
    }


    /**
     * list解决
     */
    public int[] intersect1(int[] nums1, int[] nums2) {
        List<Integer> list = Arrays.stream(nums1).mapToObj(Integer::new).collect(Collectors.toList());
        int idx = 0 ;
        int[] res = new int[nums1.length + nums1.length];
        for (int num : nums2){
            if (list.contains(num)){
                list.remove((Integer)num);
                res[idx++] = num;
            }
        }
        return Arrays.copyOfRange(res, 0, idx);
    }


    /**
     * 排序，移动指针，有点像合并有序数组
     */
    public int[] intersect(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int i = 0 , j = 0 ;
        int idx = 0 ;
        int[] res = new int[nums1.length + nums1.length];
        while (i < nums1.length && j < nums2.length){
            if (nums1[i] == nums2[j]){
                res[idx] = nums1[i];
                i++ ;
                j++ ;
                idx++;
            }else if (nums1[i] < nums2[j]){
                i++;
            }else {
                j++;
            }
        }
        return Arrays.copyOfRange(res, 0, idx);
    }


}
