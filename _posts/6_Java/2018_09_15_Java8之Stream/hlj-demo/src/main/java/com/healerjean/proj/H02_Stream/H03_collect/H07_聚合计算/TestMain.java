package com.healerjean.proj.H02_Stream.H03_collect.H07_聚合计算;

import com.healerjean.proj.H02_Stream.H03_collect.H04_groupby分组.dto.Person;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    /**
     * 1、求最大值和最小值
     */
    @Test
    public void maxAndMin() {
        List<Integer> list = Arrays.asList(1, 2, 4);

        //1、求最大值
        Optional<Integer> max = null;
        max = list.stream().max((o1, o2) -> o1 - o2);
        max = list.stream().max(Comparator.comparingInt(o -> o));
        max = list.stream().collect(Collectors.maxBy((o1, o2) -> o1 - o2));
        max = list.stream().collect(Collectors.collectingAndThen(Collectors.maxBy((o1, o2) -> o1 - o2), item -> item));


        //2、求最小值
        Optional<Integer> min = null;
        min = list.stream().min((o1, o2) -> o1 - o2);
        min = list.stream().min(Comparator.comparingInt(o -> o));
        min = list.stream().collect(Collectors.minBy((o1, o2) -> o1 - o2));
        min = list.stream().collect(Collectors.collectingAndThen(Collectors.minBy((o1, o2) -> o1 - o2), item -> item));
    }


    /**
     * 2、平均值
     */
    @Test
    public void avg() {
        //1、普通数据
        List<Integer> list = Arrays.asList(1, 2, 4);
        Integer avg = list.stream().collect(Collectors.averagingInt(o -> o)).intValue();
        System.out.println(avg);

        //2、对象
        List<Person> personList = Arrays.asList(
                new Person(1L, "a"),
                new Person(1L, "b"),
                new Person(2L, "b"),
                new Person(3L, "c"));

        avg = personList.stream().collect(Collectors.averagingLong(person -> person.getId())).intValue();
        System.out.println(avg);
    }


    /**
     * 3、求和
     */
    @Test
    public void sum() {
        //1、普通数据
        List<Integer> list = Arrays.asList(1, 2, 4);
        Integer sum = null;
        //1、reduce求和
        sum = list.stream().reduce(0, (o1, o2) -> o1 + o2);
        sum = list.stream().reduce(0, Integer::sum);
        Optional<Integer> sumOptional = list.stream().reduce(Integer::sum);

        //2、collect收集求和
        sum = list.stream().collect(Collectors.summingInt(o -> o));
    }


    
}
