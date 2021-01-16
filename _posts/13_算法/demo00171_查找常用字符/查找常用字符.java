package com.hlj.arith.demo00171_查找常用字符;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
作者：HealerJean
题目：
 给定仅有小写字母组成的字符串数组 A，返回列表中的每个字符串中都显示的全部字符（包括重复字符）组成的列表。例如，如果一个字符在每个字符串中出现 3 次，但不是 4 次，则需要在最终答案中包含该字符 3 次。
 你可以按任意顺序返回答案。
     示例 1：
             输入：["bella","label","roller"]
             输出：["e","l","l"]
     示例 2：
         输入：["cool","lock","cook"]
         输出：["c","o"]
解题思路：
*/
public class 查找常用字符 {

    @Test
    public void test(){
        String[] strArray = {"bella","label","roller"};
        System.out.println(commonChars(strArray));
    }

    public List<String> commonChars(String[] strArray) {
        int[] minfreq = new int[26];
        //填充最大值，防止对后面的Math.min产生干扰，在第一个字符串遍历结束这个就没有意义了
        Arrays.fill(minfreq, Integer.MAX_VALUE);

        for (String str: strArray) {
            int[] freq = new int[26];
            //遍历每个字符串保存到频率数组中
            for (int i = 0; i < str.length(); i++) {
                freq[str.charAt(i) - 'a'] ++;
            }

            //每次执行一个字符串就重新将外面的结果频率数组进行替换，保证结果是最小的频率
            for (int i = 0; i < 26; i++) {
                minfreq[i] = Math.min(minfreq[i], freq[i]);
            }
        }


        List<String> res = new ArrayList<>();
        for (int i = 0; i < minfreq.length; i++) {
            //当前位置有几个则加几个字符串
            for (int j = 1; j <= minfreq[i]; j++) {
                int num = i +  'a' ;
                res.add(String.valueOf((char) num));
            }
        }
        return res;
    }

}
