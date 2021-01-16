package com.hlj.arith.demo00029_最长公共前缀;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName 最长公共前缀
 * @date 2020/2/25  13:09.
 * @Description
 */
/**
作者：HealerJean
题目：最长公共前缀
     编写一个函数来查找字符串数组中的最长公共前缀。如果不存在公共前缀，返回空字符串 ""。
    示例 1:
         输入: ["flower","flow","flight"]
         输出: "fl"
    示例 2:
         输入: ["dog","racecar","car"]
         输出: ""
         解释: 输入不存在公共前缀。
说明:所有输入只包含小写字母 a-z 。
解题思路：
    第一个字符串和第二个字符串比较，相同的部分保存下来，然后这个字符串再接着和第三个字符串比较
*/
public class 最长公共前缀 {

    @Test
    public void test() {
        String[] strs = new String[]{"flower", "flow", "flight"};
        System.out.println(longestCommonPrefix(strs));

    }

    public String longestCommonPrefix(String[] strs) {
        int strsLength = strs.length;
        if (strsLength == 0) {
            return "";
        }

        //初始化第一个要比较的字符串
        String str = strs[0];
        //从第二个开始和第一个字符串从0位置开始对比
        for (int i = 1; i < strsLength; i++) {
            //开始比对字符串
            int j = 0;
            for (; j < str.length() && j < strs[i].length(); j++) {
                //如果不相等的则退出，记得此时j是没有匹配的
                if (str.charAt(j) != strs[i].charAt(j)) {
                    break;
                }
            }
            //执行到这里说明有匹配的，则从0开始截取，（因为上面break了，所以不会包含j的）
            str = str.substring(0, j);
        }
        return str;
    }

}
