package com.hlj.arith.demo00111_三角形最小路径和;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
作者：HealerJean
题目：
 给定一个三角形，找出自顶向下的最小路径和。每一步只能移动到下一行中相邻的结点上。
 相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。
 例如，给定三角形：
 [
         [2],
        [3,4],
       [6,5,7],
      [4,1,8,3]
 ]
 自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。
 说明：如果你可以只使用 O(n) 的额外空间（n 为三角形的总行数）来解决这个问题，那么你的算法会很加分。
解题思路：
*/
public class 三角形最小路径和 {


    @Test
    public void test() {
        // List<Integer> a = Arrays.asList(2);
        // List<Integer> b = Arrays.asList(3, 4);
        // List<Integer> c = Arrays.asList(5, 6, 7);
        // List<Integer> d = Arrays.asList(4, 1, 8, 3);
        // List<List<Integer>> list = Arrays.asList(a, b, c, d);

        List<Integer> a = Arrays.asList(-1);
        List<Integer> b = Arrays.asList(3, 2);
        List<Integer> c = Arrays.asList(1, -2, -1);
        List<List<Integer>> list = Arrays.asList(a, b, c);

        System.out.println(minimumTotal(list));
    }

    /**
     * 动态规划
     */
    public int minimumTotal(List<List<Integer>> triangle) {

        if (triangle.size() == 1) {
            return triangle.get(0).get(0);
        }
        int heigh = triangle.size();
        //画图找规律得出
        int width = triangle.size() * 2 - 1;
        Integer[][] nums = new Integer[heigh][width];

        for (int i = heigh - 1; i >= 0; i--) {
            //找规律得出
            int j = heigh - 1 - i;
            int index = 0;
            List<Integer> line = triangle.get(i);
            for (; j < width && index < line.size(); j = j + 2) {
                nums[i][j] = line.get(index++);
            }
        }
        Integer[][] dp = new Integer[heigh][width];
        int res = Integer.MAX_VALUE;
        dp[0][width / 2] = triangle.get(0).get(0);
        for (int i = 1; i < heigh; i++) {
            for (int j = 0; j < width; j++) {
                //如果为空，则给一个最大的数
                int leftTop = j - 1 < 0 ? Integer.MAX_VALUE : nums[i - 1][j - 1] != null ? dp[i - 1][j - 1] : Integer.MAX_VALUE;
                int rightTop = j + 1 >= width ? Integer.MAX_VALUE : nums[i - 1][j + 1] != null ? dp[i - 1][j + 1] : Integer.MAX_VALUE;
                int cur = nums[i][j] != null ? nums[i][j] : Integer.MAX_VALUE;
                //如果它的左右两边都是空，则直接选择当前的的值
                if (leftTop == rightTop && leftTop == Integer.MAX_VALUE) {
                    dp[i][j] = cur;
                } else {
                    dp[i][j] = Math.min(leftTop, rightTop) + cur;
                }
                //最后一行
                if (i == heigh - 1) {
                    res = Math.min(dp[i][j], res);
                }
            }
        }
        return res;
    }

}
