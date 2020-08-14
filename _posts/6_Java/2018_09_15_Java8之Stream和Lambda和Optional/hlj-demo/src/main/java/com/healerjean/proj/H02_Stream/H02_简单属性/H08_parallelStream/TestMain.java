package com.healerjean.proj.H02_Stream.H02_简单属性.H08_parallelStream;

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
        List<String> list = Arrays.asList("张三丰", "张无忌", "杨过");
        // 1、直接获取并发流 list.parallelStream()
        list.parallelStream().forEach(System.out::print);

        // 2、先获取普通流，然后变成并发的
        list.stream().parallel().forEach(System.out::print);
    }

}
