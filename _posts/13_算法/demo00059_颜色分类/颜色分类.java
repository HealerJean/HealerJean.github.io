package com.hlj.arith.demo00059_颜色分类;

import com.hlj.arith.z_common.ArraryPrint;
import org.junit.Test;

/**
作者：HealerJean
题目：颜色分类
解题思路：给定一个包含红色、白色和蓝色，一共 n ，原地对它们进行个元素的数组排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列
 此题中，我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
*/
public class 颜色分类 {

    @Test
    public void test(){
        int[] nums = {2,0,2,1,1,0};
        sortColors(nums);
        ArraryPrint.print(nums);
    }


    public void sortColors(int[] nums) {
        //i 代表着最后一个0的位置，
        // j 代表着第一个2 的位置
        int i = 0;
        int j = nums.length - 1;
        int index = 0;
        int tmp;
        //j最后也要进行移动，所以 是小于等于
        while (index <= j) {
            if (nums[index] == 0) {
                // 交换第 p0个和第curr个元素
                tmp = nums[i];
                nums[i] = nums[index];
                nums[index] = tmp;
                i++;
                //应为之前的走到这里，i位置肯定是1，所以替换过来之后index要++
                index++;
            } else if (nums[index] == 2) {
                tmp = nums[index];
                nums[index] = nums[j];
                nums[j] = tmp;
                //此时不知道num[j]的值，但是又替换到前面去了，所以index不会改变
                j--;
            }
            //此时num[index] == 1.这个时候只移动指针即可
            else {
                index++;
            }
        }
    }

}
