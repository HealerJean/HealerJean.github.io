package com.hlj.arith.demo00092_转变数组后最接近目标值的数组和;

import org.junit.Test;

/**
  作者：HealerJean
  题目：
  给你一个整数数组 arr 和一个目标值 target ，请你返回一个整数 value ，使得将数组中所有大于 value 的值变成 value 后，数组的和最接近  target （最接近表示两者之差的绝对值最小）。
  如果有多种使得和最接近 target 的方案，请你返回这些整数中的最小值。
  请注意，答案不一定是 arr 中的数字。
  提示： 1 <= arr.length <= 10^4   1 <= arr[i], target <= 10^5
      示例 1：
          输入：arr = [4,9,3], target = 10
          输出：3
          解释：当选择 value 为 3 时，数组会变成 [3, 3, 3]，和为 9 ，这是最接近 target 的方案。
      示例 2：
          输入：arr = [2,3,5], target = 10
          输出：5
      示例 3：
          输入：arr = [60864,25176,27249,21296,20204], target = 56803
          输出：11361
  解题思路：
 */
public class 转变数组后最接近目标值的数组和 {

    @Test
    public void test() {
        int[] arr = {2, 9, 2};
        int target = 3;
        System.out.println(findBestValue(arr, target));
    }

    /**
     * 如果选择一个阈值 value ，使得它对应的 sum 是第 1 个大于等于 target 的，那么目标值可能在 value 也可能在 value - 1。
     */
    public int findBestValue(int[] arr, int target) {
        int left = 0;
        int right = 0;
        // 因为不能选超过数组中最大的数，选择一个最大的数right
        for (int num : arr) {
            right = Math.max(right, num);
        }

        // 找到一个最接近
        while (left < right) {
            int mid = (right + left) / 2;
            int sum = calculateSum(arr, mid);
            // 计算第 1 个使得转变后数组的和大于等于 target 的阈值 threshold
            if (sum < target) {
                // 严格小于的一定不是解
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        // 比较阈值线分别定在 left - 1 和 left 的时候与 target 的接近程度
        //left 是第一个大于 target的，所以 left-1一定不大于target
        int sum1 = calculateSum(arr, left);
        int sum2 = calculateSum(arr, left - 1);
        if (target - sum2  <= sum1 - target ) {
            return left - 1;
        }
        return left;
    }

    /**
     * 计算在threshold 之下的面积
     */
    private int calculateSum(int[] arr, int threshold) {
        int sum = 0;
        for (int num : arr) {
            sum += Math.min(num, threshold);
        }
        return sum;
    }

}
