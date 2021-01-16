package com.hlj.arith.demo00078_寻找重复数;

import org.junit.Test;

/**
作者：HealerJean
题目：寻找重复数 (必然存在)
 给定一个包含 n + 1 个整数的数组 nums，其数字都在 1 到 n 之间（包括 1 和 n），可知至少存在一个重复的整数。假设只有一个重复的整数，找出这个重复的数。
     示例 1:
         输入: [1,3,4,2,2]
         输出: 2
     示例 2:
         输入: [3,1,3,4,2]
         输出: 3
     说明：不能更改原数组（假设数组是只读的）。只能使用额外的 O(1) 的空间。时间复杂度小于 O(n2) 。数组中只有一个重复的数字，但它可能不止重复出现一次。
解题思路：

 比如：1,2,3,4,5,6,2
 第一次：low = 1, hign = 6，mid = 3  count = 4    => count > mid   hign = mid
 第二次：low = 1, hign = 3, mid = 2, count = 3    => count > mid   hign = mid
 第三次：low = 1, hign = 2, mid = 1  count = 1    => count <= mid  low = mid + 1
 第四次：low = 2, hign = 2 结束
 low为最终结果

*/
public class 寻找重复数 {


    @Test
    public void test(){
        int[] nums = {1,2,3,4,5,6,2};
        System.out.println(findDuplicate(nums));
    }

    public int findDuplicate(int[] nums) {
        int n = nums.length - 1;

        //low 作为将来的结果
        int low = 1;
        int high = n;
        while (low < high) {
            int mid = (low + high) / 2;

            // 记录小于等于中间值的个数。
            int count = 0;
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] <= mid) {
                    count++;
                }
            }

            //这个数就在哪边，哪边就多
            // 计数小于等于中间值（因为上面就是保留的小于等于），这样就造成了low其实就是最终结果
            // 假设 1 2 1 ，这三个数字，
            //  右部分多，则在大于中间值范围。
            if (count <= mid) {
                low = mid + 1;
                // 计数大于中间值，则重复的数值在小于等于中间值的范围。
             // 左部分多，
            } else {
                high = mid;
            }
        }
        return low;
    }


}
