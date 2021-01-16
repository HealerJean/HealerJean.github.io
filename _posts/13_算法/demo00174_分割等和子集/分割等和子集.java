package com.hlj.arith.demo00174_分割等和子集;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 作者：HealerJean
 * 题目：
 * 给定一个只包含正整数的非空数组。是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
 * 注意: 每个数组中的元素不会超过 100,数组的大小不会超过 200
 * 示例 1:
 * 输入: [1, 5, 11, 5]
 * 输出: true
 * 解释: 数组可以分割成 [1, 5, 5] 和 [11].
 * 示例 2:
 * 输入: [1, 2, 3, 5]
 * 输出: false
 * 解释: 数组不能分割成两个元素和相等的子集.
 * 解题思路：
 */
public class 分割等和子集 {

    @Test
    public void test() {
        int[] nums = {1, 5, 11, 5};
        // int[] nums = {1, 2, 3, 5};
        // int[] nums = {1, 2, 5};
        // int[] nums = {2, 2, 3, 5};

        System.out.println(canPartition(nums));
        System.out.println(canPartition2(nums));
        System.out.println(canPartition3(nums));
    }

    /**
     * 算法1：官方
     * @param nums
     * @return
     */
    public boolean canPartition(int[] nums) {
        if (nums.length < 2) {
            return false;
        }
        int sum = 0, maxNum = 0;
        for (int num : nums) {
            sum += num;
            maxNum = Math.max(maxNum, num);
        }
        if (sum % 2 != 0) {
            return false;
        }
        int x = sum / 2;
        //如果里面有任何一个数字超过了x则，不成立
        if (maxNum > x) {
            return false;
        }


        boolean[] dp = new boolean[x + 1];
        dp[0] = true; //当 j 和 nums[i] 相等的时候
        for (int i = 0; i < nums.length; i++) {
            // 假设子集加num，和等于i，满足假设，只要dp[i - nums]存在
            // 假设子集不加num，和等于i，满足假设，只要dp[i]存在
            for (int j = x; j >= nums[i]; j--) {
                //因为 j 会 遍历多次，如果有一次存在即为true
                dp[j] = dp[j] | dp[j - nums[i]];
            }
        }
        return dp[x];
    }


    /**
     * 算法2，自己（后面类似于组合求和，提交超时了）
     * @param nums
     * @return
     */
    boolean flag = false;
    public boolean canPartition2(int[] nums) {
        if (nums.length < 2) {
            return flag;
        }
        int sum = 0, maxNum = 0;
        for (int num : nums) {
            sum += num;
            maxNum = Math.max(maxNum, num);
        }
        if (sum % 2 != 0) {
            return flag;
        }
        int x = sum / 2;
        //如果里面有任何一个数字超过了x则，不成立
        if (maxNum > x) {
            return flag;
        }

        dsfFlag(0, x, nums);
        return flag;
    }

    public void dsfFlag(int index, int target, int[] nums) {
        if (target == 0) {
            flag = true;
            return;
        }
        for (int i = index; i < nums.length; i++) {
            if (target >= nums[i]) {
                dsfFlag(i + 1, target - nums[i], nums);
            }
        }
    }


    /**
     * 2、 如果存在，求出两组
     */
    public List<List<Integer>>  canPartition3(int[] nums) {
        List<List<Integer>> res  = new ArrayList<>();
        if (nums.length < 2) {
            return res;
        }
        int sum = 0, maxNum = 0;
        for (int num : nums) {
            sum += num;
            maxNum = Math.max(maxNum, num);
        }
        if (sum % 2 != 0) {
            return res;
        }
        int x = sum / 2;
        //如果里面有任何一个数字超过了x则，不成立
        if (maxNum > x) {
            return res;
        }


        LinkedList<Integer> linkedList = new LinkedList<>();
        dsf(0, x, nums, res, linkedList);
        return res;
    }

    public void dsf(int index, int target, int[] nums, List<List<Integer>> res, LinkedList<Integer> linkedList) {
        if (target == 0) {
            res.add(new ArrayList<>(linkedList));
            return;
        }
        for (int i = index; i < nums.length; i++) {
            if (target >= nums[i]) {
                linkedList.add(nums[i]);
                dsf(i + 1, target - nums[i], nums, res, linkedList);
                linkedList.removeLast();
            }
        }
    }


}
