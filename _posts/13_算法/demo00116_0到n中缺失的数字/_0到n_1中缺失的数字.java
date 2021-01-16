package com.hlj.arith.demo00116_0到n中缺失的数字;

import org.junit.Test;


/**
作者：HealerJean
题目：
 一个长度为n-1的递增排序数组中的所有数字都是唯一的，并且每个数字都在范围0～n-1之内。在范围0～n-1内的n个数字中有且只有一个数字不在该数组中，请找出这个数字。
 示例 1:
     输入: [0,1,3]
     输出: 2
 示例 2:
     输入: [0,1,2,3,4,5,6,7,9]
     输出: 8
 解题思路：
*/
public class _0到n_1中缺失的数字 {

    @Test
    public void test(){
        int[] nums = {0,1} ;
        //9 个数字 mid = 4 相等4 则 left = mid +1
        //9 个数字 mid = 4 小于5 则 right = mid
        //不存在大于

        // int[] nums = {0,1,2,3,4,5,6,7,8} ;
        System.out.println(missingNumber(nums));
    }
    public int missingNumber(int[] nums) {

        //去除特殊情况
        int left = 0 ;
        int right = nums.length-1;
        //为了保证每个数字都经过遍历，所以 <= 最终的结果肯定是存在的，也就是left > right的时候
        while (left <= right) {
            int mid = (left + right) / 2 ;
            if (mid == nums[mid]){
                left = mid +1 ;
            }else {
                // 永远不会出现mid > nums[mid]的情况,下面表示的是 mid < nums[mid]
                right = mid-1;
            }
        }

        //想象 0,1 的时候的场景
        return left;
    }

}
