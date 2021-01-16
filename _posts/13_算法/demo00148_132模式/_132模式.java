package com.hlj.arith.demo00148_132模式;

import org.junit.Test;

import java.util.Stack;

/**
作者：HealerJean
题目：
 给定一个整数序列：a1, a2, ..., an，一个132模式的子序列 ai, aj, ak 被定义为：当 i < j < k 时，ai < ak < aj。设计一个算法，当给定有 n 个数字的序列时，验证这个序列中是否含有132模式的子序列。
 注意：n 的值小于15000。
     示例1:
         输入: [1, 2, 3, 4]
         输出: False
         解释: 序列中不存在132模式的子序列。
     示例 2:
         输入: [3, 1, 4, 2]
         输出: True
         解释: 序列中有 1 个132模式的子序列： [1, 4, 2].
     示例 3:
         输入: [-1, 3, 2, 0]
         输出: True
         解释: 序列中有 3 个132模式的的子序列: [-1, 3, 2], [-1, 3, 0] 和 [-1, 2, 0].
解题思路：
*/
public class _132模式 {


    @Test
    public void test() {
        // int[] nums = {1, 2, 3, 4};
        int[] nums = {6, 12, 3, 4, 6, 20, 10};
        System.out.println(find132pattern(nums));
    }


    public boolean find132pattern(int[] nums) {
        if (nums.length < 3) {
            return false;
        }

        int[] min = new int[nums.length];
        min[0] = nums[0];
        // 配合   if (nums[i] > min[i])  这样就保证了，栈里面是 按照大到小的顺序进入的
        for (int i = 1; i < nums.length; i++) {
            min[i] = Math.min(min[i - 1], nums[i]);
        }

        /** 保证栈里面的元素大于 min[i] 但是小于 num[i] 这样结果就成立了 */
        Stack<Integer> stack = new Stack<>();
        for (int i = nums.length - 1; i >= 0; i--) {

            // 只有当前num[i]的元素大于min[i]才有意义，这样肯定有小于num[i]的数字在前面
            if (nums[i] > min[i]) {
                while (!stack.isEmpty() &&  min[i] >=stack.peek()) {
                    stack.pop();
                }
                if (!stack.isEmpty() &&  nums[i] > stack.peek()) {
                    return true;
                }

                stack.push(nums[i]);
            }
        }
        return false;
    }

}
