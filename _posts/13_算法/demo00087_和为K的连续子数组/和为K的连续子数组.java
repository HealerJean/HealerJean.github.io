package com.hlj.arith.demo00087_和为K的连续子数组;

/**
 * @author HealerJean
 * @ClassName 最低票价
 * @date 2020/6/2  13:47.
 * @Description
 */

import org.junit.Test;
import java.util.HashMap;

/**
作者：HealerJean
题目：和为K的子数组
 给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。
 说明 :数组的长度为 [1, 20,000]。
 数组中元素的范围是 [-1000, 1000] ，且整数 k 的范围是 [-1e7, 1e7]。
     示例 1 :
         输入:nums = [1,1,1], k = 2
         输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。
 解题思路：
*/
public class 和为K的连续子数组 {

    @Test
    public void test(){
        // int nums[] = {1,1,1};
        // int nums[] = {1,2,3};
        int nums[] = {0,0,0,0,0,0,0,0,0,0};

        System.out.println(subarraySum(nums, 3));
    }

    public int subarraySum(int[] nums, int k) {
        int count = 0, sum = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        //这个很关键，只因为下面要判断是否存在，如果sum 等于 k，则表示肯定会存在，所以必须提前放入
        map.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (map.containsKey(sum - k)) {
                count += map.get(sum - k);
            }
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
}
