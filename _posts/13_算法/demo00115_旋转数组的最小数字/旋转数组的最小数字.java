package com.hlj.arith.demo00115_旋转数组的最小数字;


import org.junit.Test;

/**
作者：HealerJean
题目：
 把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。
 例如，数组 [3,4,5,1,2] 为 [1,2,3,4,5] 的一个旋转，该数组的最小值为1。  
 示例 1：
     输入：[3,4,5,1,2]
     输出：1
 示例 2：
     输入：[2,2,2,0,1]
     输出：0
解题思路：
*/
public class 旋转数组的最小数字 {


    @Test
    public void test(){
        // int[] numbers = {3,4,5,1,2} ;
        // int[] numbers = {1,3,5} ;
        // int[] numbers = {3,3,1,3} ;
        int[] numbers = {1, 3, 3};
        System.out.println(minArray(numbers));
    }

    /** 应为最后一个肯定比第一个小，所以二分判断要和最后一个判断 */
    public int minArray(int[] numbers) {
        int left = 0, right = numbers.length - 1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (numbers[mid] > numbers[right]) {
                left = mid + 1;
            }else if (numbers[mid] < numbers[left]){
                right = mid;
            }else {
                //此时 numbers[mid] == numbers[left] ，判断不出来，所以right--即可
                right--;
            }
        }
        return numbers[left];
    }


}
