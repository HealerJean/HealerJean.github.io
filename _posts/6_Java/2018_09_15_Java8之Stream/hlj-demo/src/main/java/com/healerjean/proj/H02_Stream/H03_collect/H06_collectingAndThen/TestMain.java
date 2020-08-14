package com.healerjean.proj.H02_Stream.H03_collect.H06_collectingAndThen;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;
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
    public void test() {
        List<String> list = Arrays.asList("1","2", "3");

        String str = null;
        str = list.stream() .collect(Collectors.collectingAndThen(Collectors.joining(","), item -> item + " ,"));
        System.out.println(str); //1,2,3 ,
        str = list.stream() .collect(Collectors.collectingAndThen(Collectors.joining(","), item -> "1"));
        System.out.println(str); //1

        Optional<Integer> max =  list.stream().map(Integer::valueOf).collect(Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingInt(o -> o)), item -> item));
        System.out.println(max.get()); //3

    }
}
