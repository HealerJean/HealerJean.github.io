package com.healerjean.proj.H02_Stream.H03_collect.H05_收集并拼接;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        //1、Collectors.joining()
        List<String> list = Arrays.asList("张三丰", "张无忌", "张三丰");
        String str = list.stream().collect(Collectors.joining());
        System.out.println(str); //张三丰张无忌张三丰

        //1、Collectors.joining(",")
        str = list.stream().collect(Collectors.joining(","));
        System.out.println(str); //张三丰,张无忌,张三丰
    }

}
