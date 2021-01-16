package com.hlj.arith.demo00098_分割回文串;


import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
作者：HealerJean
题目：分割回文串
 给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。
     示例:
         输入: "aab"
         输出:
         [
         ["aa","b"],
         ["a","a","b"]
         ]
解题思路：
*/
public class 分割回文串 {

    @Test
    public void test(){
        System.out.println(partition("aab"));
    }

    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        LinkedList<String> list = new LinkedList<>();
        method(res,list,s, 0);
        return res;
    }

    public void method(List<List<String>> res, LinkedList<String> list, String str, int index) {
        if (index == str.length()) {
            res.add(new ArrayList<>(list));
        }

        for (int i = index; i < str.length(); i++) {
            // 包头不包含尾
            String val = str.substring(index, i + 1);
            // 因为是分割字符串，所以这里如果不成立的话，该字符串后面的也不会成立，所以continue，（continue也不会成立）
            if (!validate(val)) {
                continue;
            }
            list.add(val);
            //截取前面的字符串后，从下一个索引位开始
            method(res, list, str, i + 1);
            list.removeLast();
        }
    }

    /**
     * 校验是否是回文
     */
    public boolean validate(String str){
        int left = 0 ;
        int right = str.length()-1;
        while (left < right){
            if (str.charAt(left) != str.charAt(right)) {
                return false ;
            }
            left++;
            right--;

        }
        return true;
    }

}
