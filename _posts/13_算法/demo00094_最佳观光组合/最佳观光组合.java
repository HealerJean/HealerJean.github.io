package com.hlj.arith.demo00094_最佳观光组合;

import org.junit.Test;

/**
作者：HealerJean
题目：最佳观光组合
 给定正整数数组 A，A[i] 表示第 i 个观光景点的评分，并且两个景点 i 和 j 之间的距离为 j - i。
 一对景点（i < j）组成的观光组合的得分为（A[i] + A[j] + i - j）：景点的评分之和减去它们两者之间的距离。返回一对观光景点能取得的最高分。
     提示：  2 <= A.length <= 50000 , 1 <= A[i] <= 1000
     示例：
         输入：[8,1,5,2,6]
         输出：11
         解释：i = 0, j = 2, A[i] + A[j] + i - j = 8 + 5 + 0 - 2 = 11
解题思路：想象 线段
*/
public class 最佳观光组合 {

    @Test
    public void test(){
        int[] nums = {1,2,4};
        System.out.println(maxScoreSightseeingPair(nums));
    }


    public int maxScoreSightseeingPair(int[] A) {
        int max = 0, cur = A[0] ;
        for (int i = 1 ; i < A.length; i++){
            max = Math.max(A[i] + cur - i, max);
            //选择一个比较大的，这样下一个和它比较的话减去的就少
            cur = Math.max(cur, A[i] + i);
        }
        return max;
    }

}
