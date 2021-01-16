package com.hlj.arith.demo00179_划分字母区间;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
作者：HealerJean
题目：
 字符串 S 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，同一个字母只会出现在其中的一个片段。返回一个表示每个字符串片段的长度的列表。
 示例 1：
     输入：S = "ababcbacadefegdehijhklij"
     输出：[9,7,8]
     解释：
         划分结果为 "ababcbaca", "defegde", "hijhklij"。  每个字母最多出现在一个片段中。
         像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少。
解题思路：
*/
public class 划分字母区间 {


    @Test
    public void test(){
        System.out.println(partitionLabels("ababcbacadefegdehijhklij"));
    }

    public List<Integer> partitionLabels(String str) {
        int[] nums = new int[26];
        // 统计每一个字符最后出现的位置
        for (int i = 0; i < str.length(); i++) {
            nums[str.charAt(i) - 'a'] = i;
        }


        List<Integer> res = new ArrayList<>();
        int left  = 0;
        int right = 0;
        for (int i = 0; i < str.length(); i++) {
            // 找到字符出现的最远边界
            right = Math.max(right, nums[str.charAt(i) - 'a']);
            //i == right 表示达到最远边界
            if (i == right) {
                res.add(right - left  + 1);
                //left 向后移动1位
                left  = right + 1;
            }
        }
        return res;
    }

}
