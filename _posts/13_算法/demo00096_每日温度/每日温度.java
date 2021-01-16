package com.hlj.arith.demo00096_每日温度;

import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

/**
作者：HealerJean
题目：每日温度
 请根据每日 气温 列表，重新生成一个列表。对应位置的输出为：要想观测到更高的气温，至少需要等待的天数。如果气温在这之后都不会升高，请在该位置用 0 来代替。
 例如，给定一个列表 temperatures = [73, 74, 75, 71, 69, 72, 76, 73]，你的输出应该是 [1, 1, 4, 2, 1, 1, 0, 0]。
 提示：气温 列表长度的范围是 [1, 30000]。每个气温的值的均为华氏度，都是在 [30, 100] 范围内的整数。
解题思路：利用栈解决， 如果从左到右的话，每次都要和为未来的数据进行比较，所以我们何不从右到左呢。
*/
public class 每日温度 {

    @Test
    public void test(){
        int [] nums = {73, 74, 75, 71, 69, 72, 76, 73};
        System.out.println(Arrays.toString(dailyTemperatures(nums)));
    }

    public int[] dailyTemperatures(int[] nums) {
        int[] res = new int[nums.length];

        //栈中的数据从从底部到上面 是从大到小的
        Stack<Integer> stack = new Stack<>();
        stack.push(nums.length-1);
        for (int i = nums.length - 2; i >= 0; i--) {
            // 当前温度 如果大于 栈顶部最小的，才回遍历 （如果小于栈顶部的，则肯定不存在比它温度高的
            if (nums[i] < nums[stack.peek()]) {
                res[i] = 1;
            } else {
                while (!stack.isEmpty() && nums[i] >= nums[stack.peek()] ) {
                    stack.pop();
                }
                //如果里面还有数据，说明此时的数据比它大
                if (!stack.isEmpty()){
                    res[i] = stack.peek() - i ;
                }
            }
            stack.push(i);
        }
        return res;
    }

}
