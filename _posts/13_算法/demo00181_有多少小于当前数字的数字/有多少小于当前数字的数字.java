package com.hlj.arith.demo00181_有多少小于当前数字的数字;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
作者：HealerJean
题目：
 给你一个数组 nums，对于其中每个元素 nums[i]，请你统计数组中比它小的所有数字的数目。
 换而言之，对于每个 nums[i] 你必须计算出有效的 j 的数量，其中 j 满足 j != i 且 nums[j] < nums[i] 。以数组形式返回答案。
     示例 1：
         输入：nums = [8,1,2,2,3]
         输出：[4,0,1,1,3]
         解释：
         对于 nums[0]=8 存在四个比它小的数字：（1，2，2 和 3）。
         对于 nums[1]=1 不存在比它小的数字。
         对于 nums[2]=2 存在一个比它小的数字：（1）。
         对于 nums[3]=2 存在一个比它小的数字：（1）。
         对于 nums[4]=3 存在三个比它小的数字：（1，2 和 2）。
     示例 2：
         输入：nums = [6,5,4,8]
         输出：[2,1,0,3]
     示例 3：
         输入：nums = [7,7,7,7]
         输出：[0,0,0,0]
 提示：
     2 <= nums.length <= 500
     0 <= nums[i] <= 100
解题思路：
 解法1：暴力
 解法2：先排序
 解法3：看提示（有规律可循）
*/
public class 有多少小于当前数字的数字 {


    @Test
    public void test() {
        int[] nums = {8, 1, 2, 2, 3};
        System.out.println(Arrays.toString(smallerNumbersThanCurrent1(nums)));
        System.out.println(Arrays.toString(smallerNumbersThanCurrent2(nums)));
        System.out.println(Arrays.toString(smallerNumbersThanCurrent(nums)));
    }

    /**
     * 解法1：暴力
     */
    public int[] smallerNumbersThanCurrent1(int[] nums) {
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (nums[i] > nums[j]){
                    res[i]++;
                }
            }
        }
        return res;
    }


    /**
     * 解法2，排序
     */
    public int[] smallerNumbersThanCurrent2(int[] nums) {
        //0 是当前数字，i是个数
        int[][] data = new int[nums.length][2];
        for (int i = 0; i < nums.length; i++) {
            data[i][0] = nums[i];
            data[i][1] = i;
        }

        //数字排序
        Arrays.sort(data, Comparator.comparingInt(v -> v[0]));

        int[] res = new int[nums.length];
        //data排序最小的可以线确定res所在位置为0
        res[data[0][1]] = 0 ;

        //i这里其实是可以代表数字的
        for (int i = 1; i < nums.length; i++) {
            //如果相等的话则和上一个数字是一样的
            if (data[i][0] ==  data[i-1][0]){
                res[data[i][1]] = res[data[i-1][1]];
            }else {
                res[data[i][1]]  = i;
            }
        }
        return res;
    }


    /**
     * 解法3：计数排序
     */
    public int[] smallerNumbersThanCurrent(int[] nums) {
        int[] data = new int[101];
        for (int i = 0; i < nums.length; i++) {
            data[nums[i]]++;
        }

        //data[i]为 计算小于等于data[i]的个数
        for (int i = 1; i <= 100; i++) {
            data[i] += data[i - 1];
        }

        int[] ret = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            //data[nums[i] - 1] 是data[nums[i]]当前数字所在位置个数，而他标示表示的是小于等于data[nums[i]] 的个数，所以需要 在-1
            ret[i] = nums[i] == 0 ? 0 : data[nums[i] - 1];
        }
        return ret;
    }

}
