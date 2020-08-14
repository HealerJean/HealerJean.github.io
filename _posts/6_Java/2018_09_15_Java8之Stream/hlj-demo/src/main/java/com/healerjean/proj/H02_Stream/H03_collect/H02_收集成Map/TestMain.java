package com.healerjean.proj.H02_Stream.H03_collect.H02_收集成Map;

import com.healerjean.proj.H02_Stream.H02_简单属性.H05_map.dto.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    /**
     * 1、普通数据
     */
    @Test
    public void test1() {
        List<String> list = Arrays.asList("张三丰", "张无忌", "岳不群", "乔峰");
        Map<String, String> hashMap = list.stream().collect(Collectors.toMap(k -> k, v -> v + "6"));
        Map<String, String> concurrentMap = list.stream().collect(Collectors.toConcurrentMap(k -> k, v -> v + "6"));
    }

    /**
     * 2、对象
     */
    @Test
    public void test2() {
        List<Person> personList = Arrays.asList(
                new Person(1L, "a"),
                // 如果收集成map，则Key 不能重复，否则会报错，
                // new Person(1L, "d"),
                new Person(2L, "b"),
                new Person(3L, "c"));


        Map<Long, String> mapName = personList.stream()
                .collect(Collectors.toMap(k -> k.getId(), v -> v.getName()));


        Map<Long, Person> mapPerson = personList.stream()
                .collect(Collectors.toMap(k -> k.getId(), v -> v));

    }


    /**
     * 2、收集并映射
     */
    @Test
    public void test3() {
        List<Person> personList = Arrays.asList(
                new Person(1L, "a"),
                new Person(2L, "b"),
                new Person(3L, "c"));

        List<String> names = personList.stream()
                .collect(Collectors.mapping(k -> k.getName(), Collectors.toList()));

    }
}
