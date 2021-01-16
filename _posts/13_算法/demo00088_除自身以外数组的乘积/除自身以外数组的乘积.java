package com.hlj.arith.demo00088_除自身以外数组的乘积;


import com.hlj.arith.z_common.ArraryPrint;
import org.junit.Test;

/**
作者：HealerJean
题目：除自身以外数组的乘积
    给你一个长度为 n 的整数数组 nums，其中 n > 1，返回输出数组 output ，其中 output[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积。
  示例:
      输入: [1,2,3,4]
      输出: [24,12,8,6]
解题思路：
 */
public class 除自身以外数组的乘积 {


    @Test
    public void test() {
        int[] nums = {1, 2, 3, 4};
        nums = productExceptSelf(nums);
        ArraryPrint.print(nums);
    }

    public int[] productExceptSelf(int[] nums) {
        int[] res = new int[nums.length];

        // res[0] 表示索引 i 左侧所有元素的乘积 ,索引位0 的左面没有元素，初始化为 1
        res[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            //      前一个数 *  前一个数的左面元素乘积
            res[i] = nums[i - 1] * res[i - 1];
        }

        //下面开始从后往前推结果 。res为将来的结果


        // R 为右侧所有元素的乘积, 刚开始右边没有元素，所以 R = 1
        int right = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            // 对于索引 i，左边的乘积为 res[i]，右边的乘积为 R
            res[i] = res[i] * right;

            // right 需要包含右边所有的乘积，所以计算下一个结果时需要将当前值乘到 right 上
            right *= nums[i];
        }
        return res;
    }
}
