package com.hlj.arith.demo00035_实现strStr;

import org.junit.Test;

import java.util.Arrays;

/**
作者：HealerJean
题目：实现 strStr()
    给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
     示例 1:
        输入: haystack = "hello", needle = "ll"
        输出: 2
    示例 2:
        输入: haystack = "aaaaa", needle = "bba"
        输出: -1
 解题思路：KMP算法
*/
public class 实现strStr {


    @Test
    public void test(){
        System.out.println(Arrays.toString(getNext("issip")));
        System.out.println(strStr("mississippi", "issip"));
    }


    public int strStr(String txt, String pattern) {
        if (pattern.length() == 0) {
            return 0;
        }
        if (txt.length() == 0){
            return -1;
        }

        //i 表示 text 中的位置，j 表示 find 中的位置
        int[] next = getNext(pattern);
        //遍历 txt 中的字符
        for (int i = 0, j = 0; i < txt.length(); i++) {
            //while放在开头，因为是刚刚开始匹配，如果不成立，next数组马上回溯
            // j!= 0 但是不相等，表示刚刚经过匹配，这里是 KMP 算法的关键点，移动位置为回溯 next[j]
            while (j != 0 && txt.charAt(i) != pattern.charAt(j)) {
                j = next[j];
            }
            //如果 i 位置和 j 位置的字符相同，待匹配字符串移动一位
            if (txt.charAt(i) == pattern.charAt(j)) {
                j++;
            }

            //在上面的if中 j++ 会比指针大1，当j等于待匹配长度的时候，表示到结尾了
            if (j == pattern.length()) {
                // i当前匹配到的地方，最后求的是txt字符串刚开始匹配的位置，所以 i - j + 1
                return i - j + 1;
            }
        }
        return -1;
    }


    /**
     * 获取next数组
     */
    public static int[] getNext(String find) {
        //如果数组长度为1，则直接返回0即可
        int[] next = new int[find.length()];
        if (find.length() ==1){
            next[0] = 0;
            return next;
        }
        //第一个和第二个是0，因为同时不存在前缀和后缀
        next[0] = 0;
        next[1] = 0;

        // 因为 next[0]  next[1] 已经确定了，要开始找 next[2]，所以初始化 i = 1
        // k 为 next[i] 当前的值，初始化的时候，next[1] = 0 ,所以k为0
        int i = 1, k = 0;
        // 所以i next[i+1] 是通过 next[i] 求的，i 不会超过 sub.length() - 1
        while (i < find.length() - 1) {
            //如果字符串位置 `i `和位置 `next[i] `处的两个字符相同，则 `next[i+1]` = `next[i] + 1`
            if (find.charAt(i) == find.charAt(k)) {
                next[i + 1] = k + 1;
                //因为上面 k 为next[i]，while下一步执行的就是i + 1
                //上面 next[i + 1] 已经给出值了，所以继续执行while的话， i 和 k 都要加 1（i指针移动加1，k为值加1）
                i++;
                k++;
            } else if (k == 0) {
                //k = 0 并且没有匹配，当然为0喽，k也不需要+1了
                next[i + 1] = 0;
                i++;
            } else {
                // 往前好回溯，这个时候k是大于0的，但是上面第一个比较的时候，没有成功。
                // 为了再类似于暴力法那样重新开始匹配，按照我们找出的规律往前回溯，代表性例子就知道了
                k = next[k];
            }
        }
        return next;
    }


    /**
     * 暴力
     */
    public int strStr2(String txt, String pattern) {
        if (pattern.length() == 0) {
            return 0;
        }
        if (txt.length() == 0) {
            return -1;
        }


        int  i = 0 ;
        int j = 0 ;
        int index = 0 ;
        while (j < pattern.length() && index <= txt.length() - pattern.length()) {
            if (txt.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    return i - pattern.length();
                }
            } else {
                index++;
                j = 0;
                i = index;
            }
        }
        return -1 ;
    }

}
