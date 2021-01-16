package com.hlj.arith.demo00031_三数之和;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
作者：HealerJean
题目：三数之和
    给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有满足条件且不重复的三元组。
    注意：答案中不可以包含重复的三元组。
 示例：
     给定数组 nums = [-1, 0, 1, 2, -1, -4]，
     输出：
        [ [-1, 0, 1], [-1, -1, 2] ]
解题思路：
 先排序，从左到右遍历，对于每个 `k` 值：
 如果三数之和大于 0，j 左移会使得和减小，甚至等于 0，故 j 左移；
 如果三数之和小于 0，i 右移会使得和增大，甚至等于 0，故 i 右移；
 如果三数之和等于 0，将 [`nums[k]`,`nums[i]`,`nums[j]`] 加入到结果中，同时判断是否有重复的`i`或者`j`，`i `右移，`j `左移，寻找下一组可能结果。
*/
public class 三数之和 {

    @Test
    public void test() {
        // int[] nums = {0,0,0};
        // int[] nums = {0,0,0,0};
        int[] nums = {-1, 0, 1, 2, -1, -4};
        // int[] nums = {-4,-2,1,-5,-4,-4,4,-2,0,4,0,-2,3,1,-5,0};
        System.out.println(threeSum(nums));
    }

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();

        // 1、先排序
        Arrays.sort(nums);

        // k 为当前数据 i为左下标，j为右下标
        int i, j, temp;
        for (int k = 0; k < nums.length - 2; k++) {
            // 如果当前数字大于0，因为 ( i > k && j > k  )则三数之和一定大于0，所以结束循环
            if (nums[k] > 0) {
                break;
            }
            // 去重复
            if (k > 0 && nums[k] == nums[k - 1]) {
                continue;
            }
            i = k + 1;
            j = nums.length - 1;

            while (i < j) {
                temp = nums[k] + nums[i] + nums[j];
                if (temp == 0) {
                    list.add(Arrays.asList(nums[k], nums[i], nums[j]));
                    while (i < j && nums[i] == nums[i + 1]) {
                        //这个时候i还是重复的，所以下面还会进行一次i++
                        i++;
                    }
                    while (i < j && nums[j] == nums[j - 1]) {
                        //这个时候j还是重复的，所以下面还会进行一次j++
                        j--;
                    }
                    i++;
                    j--;
                } else if (temp > 0) {
                    j--;
                } else if (temp < 0) {
                    i++;
                }
            }
        }

        return list;
    }

}
