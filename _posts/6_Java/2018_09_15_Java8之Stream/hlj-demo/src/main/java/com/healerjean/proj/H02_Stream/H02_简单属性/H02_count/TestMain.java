package com.healerjean.proj.H02_Stream.H02_简单属性.H02_count;

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

    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");
        Long count = list.stream()
                .filter(str -> str.length() == 3)
                .filter(str -> str.startsWith("张"))
                .count();
    }
}
