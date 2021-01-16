package com.hlj.arith.demo00143_电话号码的字母组合;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HealerJean
 * @ClassName 电话号码的字母组合
 * @date 2020/8/26  10:32.
 * @Description
 */
/**
作者：HealerJean
题目：
 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 示例:
     输入："23"
     输出：["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
解题思路：
*/
public class 电话号码的字母组合 {


    @Test
    public void test() {
        System.out.println(letterCombinations("23"));
    }

    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits == null || digits.length() == 0) {
            return res;
        }

        Map<Character, String> map = new HashMap<Character, String>() {{
            put('2', "abc");
            put('3', "def");
            put('4', "ghi");
            put('5', "jkl");
            put('6', "mno");
            put('7', "pqrs");
            put('8', "tuv");
            put('9', "wxyz");
        }};

        dfs(0, "", digits, map, res);
        return res;
    }

    /**
     *
     * @param index 表示的是数字字符串所代表的指针
     */
    void dfs(int index, String str, String digits, Map<Character, String> map, List<String> res) {
        if (index == digits.length()) {
            res.add(str);
            return;
        }

        //curStr 表示的是当前数字锁代表的 字符串
        String curStr = map.get(digits.charAt(index));
        //i 当前数字所代表的字符串指针，循环它去获得下一个指针
        for (int i = 0; i < curStr.length(); i++) {
            str +=  curStr.charAt(i);

            //走向下一个节点
            dfs(index + 1, str, digits, map, res);
        }
    }
}
