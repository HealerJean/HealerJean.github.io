package com.hlj.arith.demo00046_多数元素;


import org.junit.Test;

/**
作者：HealerJean
题目：多数元素
 给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数大于 ⌊ n/2 ⌋ 的元素。
    注意：你可以假设数组是非空的，并且给定的数组总是存在多数元素。
    示例 1:
        输入: [3,2,3]
        输出: 3
    示例 2:
        输入: [2,2,1,1,1,2,2]
        输出: 2
解题思路：利用遇到两个不同元素则都删除，最后剩下的就是多数元素
*/
public class 多数元素 {

    @Test
    public void test(){
        int[] nums = {2,2,2,2,1,2,2} ;
        System.out.println(majorityElement(nums));
    }


    public int majorityElement(int[] nums) {
        int num = nums[0];
        //count 记录是否要删除
        int count = 1;
        for(int i = 1; i < nums.length ; i++){
            if(nums[i] == num){
                //当前相等的count个数
                count++;
            }else{
                if(count > 1){
                  // 这里虽然显示的是--，但是其实减去了2个数据，当前的i和上一个已经匹配相等的数据
                    count--;
                    //如果此时不相等，count等于1，则表示，目前仅存的两个是相等的，要删除，
                    // 同时i要重新开始匹配 i + 1 + 1 为新开始的数字（要先i ++ 因为后面for'循环还有一个i ++）
                }else if (count == 1){
                    num = nums[i + 1];
                    count = 1;
                    i++;
                }
            }
        }
        return num;
    }


}
