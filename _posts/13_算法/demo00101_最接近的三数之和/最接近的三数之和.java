package com.hlj.arith.demo00101_最接近的三数之和;


import org.junit.Test;

import java.util.Arrays;

/**
作者：HealerJean
题目：最接近的三数之和
 给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target 最接近。返回这三个数的和。假定每组输入只存在唯一答案。
     示例：
         输入：nums = [-1,2,1,-4], target = 1
         输出：2
         解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2) 。
解题思路：其实和3数之和的解法差不多，（有一些可以优化的地方，就是类似于三数之的重复判断）
*/
public class 最接近的三数之和 {

    @Test
    public void test(){
        int[] nums = {1,1,-1,-1,3} ;
        int target = 3 ;
        System.out.println(threeSumClosest(nums, target));
    }

    public int threeSumClosest(int[] nums, int target) {

        //从小到达排列
        Arrays.sort(nums);
        int res = 0;
        int differ = Integer.MAX_VALUE;

        for (int k = 0; k < nums.length - 2; k++) {

            int i = k +1 ;
            int j = nums.length-1;
            while (i < j ){
                int sum = nums[k] + nums[i] + nums[j];

                //确定差值，和三数之和不同的地方
                int tempDiffer = Math.abs(sum-target);
                if (tempDiffer < differ){
                    res = sum;
                    differ = tempDiffer ;
                }


                if (sum == target){
                    return target;
                }else if (sum > target){
                     j--;
                }else {
                    i++;
                }
            }

        }
        return res;
    }
}
