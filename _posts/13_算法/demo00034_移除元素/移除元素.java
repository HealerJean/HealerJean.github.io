package com.hlj.arith.demo00034_移除元素;

import org.junit.Test;

/**
作者：HealerJean
题目：移除元素
    给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
注意：不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 示例 1:
     给定 nums = [3,2,2,3], val = 3,
     函数应该返回新的长度 2, 并且 nums 中的前两个元素均为 2。
     ```
 示例 2:
     给定 nums = [0,1,2,2,3,0,4,2], val = 2,
     函数应该返回新的长度 5, 并且 nums 中的前五个元素为 0, 1, 3, 0, 4。
 解题思路：一次遍历，很简单的，看代码吧
*/
public class 移除元素 {

    @Test
    public void test(){
        int[] nums = {3,2,3,2};
        System.out.println(removeElement(nums, 3 ));
    }
    public int removeElement(int[] nums, int val) {
        //i 数组下标
        int i = 0 ;
        for (int j = 0 ; j < nums.length ; j ++){
            if (nums[j] != val){
                nums[i] = nums[j];
                i ++ ;
            }
        }
        //返回数组个数，上面最后i++退出，其实就已经数数组个数了
        return i ;
    }


}
