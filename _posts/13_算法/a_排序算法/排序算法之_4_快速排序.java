package com.hlj.arith.a_排序算法;

import org.junit.Test;

import java.util.Arrays;

/**
作者：HealerJean
题目：
 快速排序 从两端向中间靠拢
 1．先从数列中取出一个数作为基准数。
 2．分区过程，将比这个数大的数全放到它的右边，小于或等于它的数全放到它的左边。
 3．再对左右区间重复第二步，直到各区间只有一个数。
解题思路：
*/
public class 排序算法之_4_快速排序 {

    @Test
    public void test() {
        int nums[] = {49, 38, 65, 97, 76, 13, 27, 50};
        快速排序(nums);
        System.out.println(Arrays.toString(nums));
    }


    public void 快速排序(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        sort(nums, low, high);
    }


    public void sort(int[] nums, int low, int high) {
        int i = low, j = high;
        //当i小于J的时候执行，也就是说low必须小于hign
        if (i < j) {
            //po为基数
            int po = nums[low];


            //每次当i比j小的时候小的时候开始比较，当它大于的时候，就会开始下一次排序
            while (i < j) {


                //肯定是i<j的 ，一旦po小于后面的，那么j就减1， 从后往前推 j--
                while (i < j && po < nums[j]) {
                    //这里是while循环 ，一定到最后是po>a[j] 所以一定到了最后是i<j基本上毫无疑问的
                    j--;
                }

                //通过上面的while，如果还满足i < j ，则肯定是po >= nums[j]
                if (i < j) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                    i++;
                }


                // 如果前面的大于后面的，肯定要推进的  从前往后推 i++
                while (i < j && po > nums[i]) {
                    i++;
                }

                if (i < j) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                    j--;
                }
            }

            //这个时候i就比j大了，所以需要进行下一趟排序。直到最终的结果low =j-1 j+1=high
            //从小到J 分成两组
            sort(nums, low, j - 1);
            //从J到到 分成两组
            sort(nums, j + 1, high);
        }
    }





}
