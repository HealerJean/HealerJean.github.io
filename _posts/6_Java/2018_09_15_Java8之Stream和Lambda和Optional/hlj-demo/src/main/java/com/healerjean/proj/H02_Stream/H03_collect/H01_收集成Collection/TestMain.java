package com.healerjean.proj.H02_Stream.H03_collect.H01_收集成Collection;

import com.healerjean.proj.H02_Stream.H03_collect.H04_groupby分组.dto.Person;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    /**
     * 1、收集成Collection
     */
    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        list.add("张三丰");
        list.add("张无忌");
        list.add("岳不群");
        list.add("乔峰");
        list.add("爱因斯坦");
        // Collectors.toList()
        List<String> setList = list.stream().collect(Collectors.toList());
        // Collectors.toSet()
        Set<String> setSet = null;
        setSet = list.stream().collect(Collectors.toSet());
        setSet = list.stream().collect(Collectors.toCollection(() -> new HashSet<>()));

    }

    /**
     * 2、去重
     */
    @Test
    public void test2() {
        List<Person> personList = Arrays.asList(
                new Person(1L, "a"),
                new Person(1L, "b"),
                new Person(2L, "b"),
                new Person(2L, "b"));

        //单属性去重 1
        List<Person> collect = personList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Person::getName))),
                        ArrayList::new));
        System.out.println(collect);

        //单属性去重 2
        collect = new ArrayList<>(personList.stream().collect(Collectors.toMap(Person::getName, Function.identity(), (existing, replacement) -> existing)).values());
        System.out.println(collect);

        //多属性去重 1
        collect = personList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getName() + ":" + o.getId()))),
                        ArrayList::new));
        System.out.println(collect);
    }
}
