package com.healerjean.proj.H02_Stream.H02_简单属性.H09_distinct;

import org.junit.Test;

import java.util.Arrays;
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
        List<String> list = Arrays.asList("张三丰", "张无忌", "张三丰");
        list.stream().distinct().forEach(System.out::print);
    }

}
