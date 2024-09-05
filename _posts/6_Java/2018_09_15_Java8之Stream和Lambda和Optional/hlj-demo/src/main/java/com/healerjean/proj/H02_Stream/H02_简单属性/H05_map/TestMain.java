package com.healerjean.proj.H02_Stream.H02_简单属性.H05_map;

import com.healerjean.proj.H02_Stream.H02_简单属性.H05_map.dto.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

        // 1、普通数据
        List<String> strList = Arrays.asList("10", "20", "30");
        List<Integer> numList = strList.stream()
                .map(s -> Integer.parseInt(s))
                .map(s -> s + 1000)
                .collect(Collectors.toList());


        //2、对象
        List<Person> personList = Arrays.asList(
                new Person(1L, "a"),
                new Person(2L, "b"),
                new Person(3L, "c"));
        //二者一样
        List<String> names = personList.stream().map(Person::getName).collect(Collectors.toList());
        names = personList.stream().collect(Collectors.mapping(item -> item.getName(), Collectors.toList()));

        List<Long> ids = personList.stream().map(Person::getId).collect(Collectors.toList());

    }

    @Test
    public void testFlatMap(){
   //2、对象
        List<Person> personList = Arrays.asList(
                new Person(1L, "a"),
                new Person(2L, "b"),
                new Person(3L, "c"));

         List<Person> personList2 = Arrays.asList(
                new Person(2L, "b"),
                new Person(3L, "c"),
                 new Person(4L, "c"));

        List<List<Person>> list = new ArrayList<>();
        list.add(personList2);
        list.add(personList);
        List<String> collect = list.stream().flatMap(Collection::stream).map(Person::getName).collect(Collectors.toList());
    }

}
