package com.hlj.arith.demo0006_两数之和;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 题目：
 */

public class 两数之和 {

    @Test
    public void test() {
        int[] nums = new int[]{1, 2, 3, 5};
        System.out.println(Arrays.toString(twoSum(nums, 3)));
    }

    /**
     * 方法一：暴力法
     * 解题思路：两个for循环走一遍，遍历每个元素 x，并查找是否存在一个值与 target - x 相等的目标元素
     * 时间复杂度：O(n^2)：对于每个元素，我们试图通过遍历数组的其余部分来寻找它所对应的目标元素，这将耗费 O(n) 的时间。因此时间复杂度为 O(n^2)
     * 空间复杂度：O(1)。
     */
    public int[] method(int[] nums, int target) {
        int size = nums.length;
        for (int i = 0; i <= size - 2; i++) {
            for (int j = i + 1; j <= size - 1; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    /**
     * 方法一：一遍哈希表
     * 解题思路：利用一个map将所有的值填进去
     * 时间复杂度：O(n)，我们只遍历了包含有 n 个元素的列表一次。在表中进行的每次查找只花费O(1) 的时间。
     * 空间复杂度：O(n)，所需的额外空间取决于哈希表中存储的元素数量，该表最多需要存储 n 个元素。
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }


}
