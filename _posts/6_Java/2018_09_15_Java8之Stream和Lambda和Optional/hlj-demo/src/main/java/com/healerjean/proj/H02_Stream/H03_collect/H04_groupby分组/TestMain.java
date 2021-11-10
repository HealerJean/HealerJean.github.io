package com.healerjean.proj.H02_Stream.H03_collect.H04_groupby分组;

import com.healerjean.proj.H02_Stream.H03_collect.H04_groupby分组.dto.Person;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {


    public static void main(String[] args) {
        List<Person> personList = Arrays.asList(
                new Person(1L, null),
                new Person(1L, "b"),
                new Person(1L, null),
                new Person(2L, "b"),
                new Person(3L, "c"));

        //1、分组，组内数据收集成list
        // Map<Long, List<Person>> mapListPerson = personList.stream()
        //         .collect(Collectors.groupingBy(item -> item.getId(), Collectors.toList()));
        // System.out.println(mapListPerson);


        Map<String, List<Person>> mapListPerson = personList.stream()
                .collect(Collectors.groupingBy(item -> item.getId() + "_" +  (item.getName() == null ? "####" : item.getName()), Collectors.toList()));
        System.out.println(mapListPerson);
    }

    @Test
    public void test1() {
        List<Person> personList = Arrays.asList(
                new Person(1L, null),
                new Person(1L, "b"),
                new Person(1L, null),
                new Person(2L, "b"),
                new Person(3L, "c"));

        //1、分组，组内数据收集成list
        // Map<Long, List<Person>> mapListPerson = personList.stream()
        //         .collect(Collectors.groupingBy(item -> item.getId(), Collectors.toList()));
        // System.out.println(mapListPerson);


        Map<String, List<Person>> mapListPerson = personList.stream()
                .collect(Collectors.groupingBy(item -> item.getId() + "_" + item.getName(), Collectors.toList()));
        System.out.println(mapListPerson);

        //1.2、组内数据再映射
        Map<Long, List<String>> mapNameListPerson = personList.stream()
                .collect(Collectors.groupingBy(item -> item.getId(), Collectors.mapping(item -> item.getName(), Collectors.toList())));
        System.out.println(mapNameListPerson);

        //2、分组计数
        Map<Long, Long> count = personList.stream()
                .collect(Collectors.groupingBy(item -> item.getId(), Collectors.counting()));
        System.out.println(count);

    }
}
