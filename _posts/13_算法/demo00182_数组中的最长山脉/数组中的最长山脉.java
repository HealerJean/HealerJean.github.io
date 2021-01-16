package com.hlj.arith.demo00182_数组中的最长山脉;

import org.junit.Test;

/**
作者：HealerJean
题目：
 我们把数组 A 中符合下列属性的任意连续子数组 B 称为 “山脉”：
 B.length >= 3
 存在 0 < i < B.length - 1 使得 B[0] < B[1] < ... B[i-1] < B[i] > B[i+1] > ... > B[B.length - 1]
 （注意：B 可以是 A 的任意子数组，包括整个数组 A。）给出一个整数数组 A，返回最长 “山脉” 的长度。如果不含有 “山脉” 则返回 0。
     示例 1：
         输入：[2,1,4,7,3,2,5]
         输出：5
         解释：最长的 “山脉” 是 [1,4,7,3,2]，长度为 5。
     示例 2：
         输入：[2,2,2]
         输出：0
         解释：不含 “山脉”。
解题思路：
*/
public class 数组中的最长山脉 {


    @Test
    public void test(){
        // int[] nums = {2,1,4,7,3,2,5};
        int[] nums = {1,2,1};

        System.out.println(longestMountain(nums));
    }

    public int longestMountain(int[] nums) {
        int n = nums.length;
        int max = 0;
        int left = 0;
        //山脉长度最长为 3 ，所以需要包装 left + 2 < n
        while (left + 2 < n) {
            //将右侧山脚的 right 的初始值置为1left+1，随后不断地向右移动 right，直到不满足 A[right] < A[right+1] 为止，
            // 此时：如果 right=n−1，说明我们已经移动到了数组末尾，已经无法形成山脉了；否则，right 指向的可能是山顶。我们需要额外判断是有满足 A[right] > A[right+1]，这是因为如果两者相等，那么 right 指向的就不是山顶了。
            int right = left + 1;
            //必须保证left小于left+1.这样才能保证left是山脚
            if (nums[left] < nums[left + 1]) {
                //如果right不是数组最后一个节点，并且满足山的左侧，nums[right] < nums[right + 1] 则要上坡
                while (right + 1 < n && nums[right] < nums[right + 1]) {
                    right++;
                }

                //如果right不是数组最后一个节点，并且nums[right] > nums[right + 1] 成立，说明要下坡了
                if (right + 1 < n  && nums[right] > nums[right + 1]) {
                    //开始下坡
                    while (right + 1 < n && nums[right] > nums[right + 1]) {
                        right++;
                    }
                    max = Math.max(max, right - left + 1);
                } else {
                    //到了这里说明有可能是最后一个节点，也有可能会是和下一个数字相等（nums[right] == nums[right + 1]）了
                    right++;
                }
            }

            //right当成left继续查找下一个山脉
            left = right;
        }
        return max;
    }

}
