package com.healerjean.proj.H02_Stream.H02_简单属性.H06_concat;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void demo() {
        List<String> list1=  Arrays.asList("张三丰");
        List<String> list2 = Arrays.asList("张无忌", "赵敏");

        List<String> list = Stream.concat(list2.stream(), list2.stream()).collect(Collectors.toList());
    }

}
