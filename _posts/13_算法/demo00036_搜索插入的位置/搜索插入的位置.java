package com.hlj.arith.demo00036_搜索插入的位置;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName 搜索插入的位置
 * @date 2020/3/12  19:31.
 * @Description
 */
public class 搜索插入的位置 {

    @Test
    public void test() {
        int[] nums = {1, 3, 5, 6};
        System.out.println(searchInsert(nums, 2));
    }

    public int searchInsert(int[] nums, int target) {
        //最差的情况数组会全部匹配一遍
        for (int i = 0; i < nums.length; i++) {
            //相等的时候肯定成立
            //当匹配到nums数组比目标值大的时候，表示肯定在它前面，这个数组从i位置向后移动，所以当前i的位置就是目标位置
            if (nums[i] >= target) {
                return i;
            }
        }

        //知道for循环完毕，还没有匹配，target肯定超过了所有的数组中值的大小
        return nums.length;
    }

}
