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
题目：删除排序数组中的重复项_2
 给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素最多出现两次，返回移除后数组的新长度。
 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
解题思路：
*/
public class 删除排序数组中的重复项_2 {

    @Test
    public void test() {
        int[] nums = {1,1,1,2,2,3};
        System.out.println(removeDuplicates(nums));
    }

    public int removeDuplicates(int[] nums) {
        if (nums.length == 0){
            return 0;
        }

        //数组下标，nums[0] 肯定是第一个
        int count = 1 ;
        int i = 0 ;
        for (int j = 1 ; j < nums.length ;j ++ ){
            if (nums[i] != nums[j]){
                //当前数组下标
                nums[i+1] = nums[j];
                i++;
                //如果不相等的情况下重新开始计算
                count = 1 ;
            }else {
                //只有当count等于1，而且继续相等的情况下，数组才会加，而且此时 count会变成2，后面的无论怎么相等都不会加到数组中去
                if (count == 1){
                    nums[i+1] = nums[j];
                    i++;
                    count = 2;
                }
            }
        }
        //i是当前数组下标，这里是最终数组个数
        return i + 1 ;
    }

}
