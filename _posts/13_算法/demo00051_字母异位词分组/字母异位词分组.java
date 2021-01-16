package com.hlj.arith.demo00051_字母异位词分组;

import org.junit.Test;

import java.util.*;

/**
 * @author HealerJean
 * @ClassName 字母异位词分组
 * @date 2020/4/22  10:14.
 * @Description
 */
/**
作者：HealerJean
题目： 字母异位词分组
 给定一个字符串数组，将字母异位词组合在一起。字母异位词指字母相同，但排列不同的字符串。
     示例:

         输入: ["eat", "tea", "tan", "ate", "nat", "bat"]
         输出:
         [
         ["ate","eat","tea"],
         ["nat","tan"],
         ["bat"]
         ]
解题思路：遍历这个数组，然后将每个字符串内部的char进行排序。接着将这个字符串作为key放入数组中，value为同属性的字符串
*/
public class 字母异位词分组 {


    @Test
    public void test(){
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println(groupAnagrams(strs));
        // new ArrayList<>(map.values());
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        List<List<String>> ret = new ArrayList<>();
        for (int i = 0 ; i < strs.length ; i++){
            String string  = strs[i] ;
            char[] characters = string.toCharArray();
            Arrays.sort(characters);
            if (map.containsKey(String.valueOf(characters))){
                map.get(String.valueOf(characters)).add(string);
            }else {
                List<String> list = new ArrayList<>();
                list.add(string);
                map.put(String.valueOf(characters), list);
            }
        }

        for (Object key: map.keySet()){
            ret.add(map.get(key));
        }
        //return new ArrayList<>(map.values());
        return ret;
    }

}
