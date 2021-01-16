package com.hlj.arith.demo00018_动态规划.Z03_交叉队列;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName Z03_交叉队列
 * @Date 2020/1/20  17:21.
 * @Description
 */
public class Z03_交叉队列 {

    @Test
    public void test(){
        System.out.println(method());
    }

    public String method() {
        String line = "aabcc,dbbca,aadbbcbcac";
        String[] arr1 = line.split(",");
        int len1 = arr1[0].length();
        int len2 = arr1[1].length();
        int len3 = arr1[2].length();

        //判断基本的关系
        if (len3 != len1 + len2) {
            return false + "";
        }
        if (len1 == 0) {
            return (arr1[1] == arr1[2]) + "";
        }
        if (len2 == 0) {
            return (arr1[0] == arr1[2]) + "";
        }


        //开始真正代码

        int[][] dp = new int[len1 + 1][len2 + 1];
        //第一个点必然可达
        dp[0][0] = 1;
        // 初始化数组第1列， 如果第第一列有一个为0，那么肯定不可达， 后续的都为0，此处代码甚好
        for (int i = 1; i <= len1; i++) {
            if (arr1[0].charAt(i - 1) == arr1[2].charAt(i - 1)) {
                dp[i][0] = dp[i - 1][0];
            }
        }
        //初始化数组第1hang
        for (int i = 1; i <= len2; i++) {
            if (arr1[1].charAt(i - 1) == arr1[2].charAt(i - 1)) {
                dp[0][i] = dp[0][i - 1];
            }
        }



        // dp
        for (int i = 1; i < len1 + 1; i++) {
            for (int j = 1; j < len2 + 1; j++) {
                int t = i + j;
                //这行代码相当于我们已经默认了第一个数据是arr1[1] 的第一个子母，而这个子母在上面已经初始化了
                if (arr1[0].charAt(i - 1) == arr1[2].charAt(t - 1)) {
                    dp[i][j] = dp[i - 1][j] | dp[i][j];
                }
                if (arr1[1].charAt(j - 1) == arr1[2].charAt(t - 1)) {
                    dp[i][j] = dp[i][j - 1] | dp[i][j];
                }
            }
        }
        // 返回处理后的结果
        if (dp[len1][len2] == 1) {
            return true + "";
        }
        return false + "";
    }


}
