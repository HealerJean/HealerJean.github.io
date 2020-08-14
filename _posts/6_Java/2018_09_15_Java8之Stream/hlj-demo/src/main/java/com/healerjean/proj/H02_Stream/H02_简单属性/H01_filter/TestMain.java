package com.healerjean.proj.H02_Stream.H02_简单属性.H01_filter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    /**
     * filter 过滤 ，返回值为Stream
     * 功能： 查找到list中长度等于3 并且是以 张开头的
     * 解答： ilter 参数是一个predicate 断言 也就是能产生boolean结果的过滤规则,
     * 案例：打印长度为3，以张开头的字符串
     */
    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");

        list.stream()
                .filter(str -> str.length() == 3)
                .filter(str -> str.startsWith("张"))
                .forEach(s -> {
                    System.out.printf(s + ",");
                });
        //输入答案
        //张三丰,张无忌,
    }

}
