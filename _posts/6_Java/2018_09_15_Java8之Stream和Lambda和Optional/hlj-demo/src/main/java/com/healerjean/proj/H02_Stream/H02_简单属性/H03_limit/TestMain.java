package com.healerjean.proj.H02_Stream.H02_简单属性.H03_limit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */
public class TestMain {

    /**
     * limit 获取执行结果的前几个 ,，返回值为Stream
     */
    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");
        list = list.stream().limit(2).collect(Collectors.toList());
    }

}
