package com.hlj.arith.demo00033_删除排序数组中的重复项;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName 删除排序数组中的重复项_1
 * @date 2020/3/9  18:22.
 * @Description
 */
/**git
作者：HealerJean
题目：删除排序数组中的重复项_1
 给定一个排序数组，你需要在 原地 删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。

示例 1:
    给定数组 nums = [1,1,2],函数应该返回新的长度 2, 并且原数组 nums 的前两个元素被修改为 1, 2。
示例 2:
    给定 nums = [0,0,1,1,1,2,2,3,3,4],函数应该返回新的长度 5, 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4。

解题思路：非常简单，一次遍历即可
*/
public class 删除排序数组中的重复项_1 {

    @Test
    public void test() {
        int[] nums = {1,1,2};
        System.out.println(removeDuplicates(nums));
    }

    public int removeDuplicates(int[] nums) {
        if (nums.length == 0){
            return 0;
        }

        //数组下标，nums[0] 肯定是第一个
        int i = 0 ;
        for (int j = 1 ; j < nums.length ;j ++ ){
            if (nums[i] != nums[j]){
                //当前数组下标
                nums[i+1] = nums[j];
                i++;
            }
        }
        //i是当前数组下标，这里是最终数组个数
        return i + 1 ;
    }

}
