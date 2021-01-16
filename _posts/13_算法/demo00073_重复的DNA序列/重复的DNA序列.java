package com.hlj.arith.demo00073_重复的DNA序列;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
作者：HealerJean
题目：
 所有 DNA 都由一系列缩写为 A，C，G 和 T 的核苷酸组成，例如：“ACGAATTCCG”。在研究 DNA 时，识别 DNA 中的重复序列有时会对研究非常有帮助。
 编写一个函数来查找 DNA 分子中所有出现超过一次的 10 个字母长的序列（子串）。
 示例：
     输入：s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
     输出：["AAAAACCCCC", "CCCCCAAAAA"]
解题思路：使用Set ，遍历字符串，每次返将10个长度的放入，如果重复的话说明是我们想要的，放到List中去，如果List存在就不放
*/
public class 重复的DNA序列 {


    @Test
    public void test(){
        System.out.println(findRepeatedDnaSequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"));
    }


    public List<String> findRepeatedDnaSequences(String s) {

        List<String> res = new ArrayList<>();
        if (s.length() <= 10) {
            return res;
        }

        HashSet<String> set = new HashSet<>();
        //保证i是起点的时候 有10个字符
        for (int i = 0; i + 10 <= s.length(); i++) {
            //包头不包尾，截取长度为10
            String str = s.substring(i, i + 10);
            if (!set.add(str)) {

                //，走到这里说明set 说明 str重复了 。加入结果集中不包含的
                if (!res.contains(str)) {
                    res.add(str);
                }
            }

        }
        return res;
    }

}
