package com.hlj.arith.demo00043_缺失的第一个正数;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 作者：HealerJean
 题目：缺失的第一个正数
    给你一个未排序的整数数组，请你找出其中没有出现的最小的正整数。
 解题思路：
     示例 1:
        输入: [1,2,0]
        输出: 3
     示例 2:
        输入: [3,4,-1,1]
        输出: 2
     示例 3:
        输入: [7,8,9,11,12]
        输出: 1
 */
public class 缺失的第一个正数 {

    @Test
    public void test() {
        // int[] nums = {1, 2, 8, 3 , 6, 7};
        // int[] nums = {1, 2, 0};
        int[] nums = {1, 2, 3,4,4};

        System.out.println(firstMissingPositive(nums));
    }

    public int firstMissingPositive(int[] nums) {
        int n = nums.length;

        // 检查 1 是否存在于数组中。如果没有，则已经完成，1 即为答案。
        boolean contains1 = false;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 1) {
               contains1 = true;
                break;
            }
        }
        //此时如果不包含1，那么结果只能是1
        if (!contains1) {
            return 1;
        }
        // 通过了上面的，说明里面肯定有1，


        // 如果数组只有一个数字， 那就只能是 nums = [1]，答案即为 2 。
        if (n == 1) {
            return 2;
        }


        //  通过了上面的，说明里面肯定有1， 所以不用理会1。直接用 1 替换负数，0， 和大于 n 的数，这样 在转换以后，nums 只会包含正数
        for (int i = 0; i < n; i++) {
            if ((nums[i] <= 0) || (nums[i] > n)) {
                nums[i] = 1;
            }

        }




        // 使用索引和数字符号作为检查器
        // 如果 nums[1] 是负数表示在数组中出现了数字 `1`
        // 如果 nums[2] 是正数 表示数字 2 没有出现
        for (int i = 0; i < n; i++) {
            // 如果发现了一个数字， 改变第 i 个元素的符号
            int a = Math.abs(nums[i]);
            // 注意重复元素只需操作一次 a如果等于n就超过数组的大小了，所以放到了nums[0]中。
            if (a == n) {
                // 这里的结果非常有必要,如果整个数组都为负数，那么将来的结果就是n + 1
                nums[0] = - Math.abs(nums[0]) ;
            } else {
                nums[a] = - Math.abs(nums[a]) ;
            }
        }

        // 现在第一个正数的下标
        // 就是第一个缺失的数
        for (int i = 1; i < n; i++) {
            if (nums[i] > 0) {
                return i;
            }
        }

        //如果出现了这种情况，很明显就是n了，因为 举例  1,2,3,3    1 ,-1, -1, -1, 这样n是4 也就是结果
        if (nums[0] > 0) {
            return n;
        }

        //如果上面都不成立，那么数字是一个从1++ 的数组
        return n + 1;
    }




}
