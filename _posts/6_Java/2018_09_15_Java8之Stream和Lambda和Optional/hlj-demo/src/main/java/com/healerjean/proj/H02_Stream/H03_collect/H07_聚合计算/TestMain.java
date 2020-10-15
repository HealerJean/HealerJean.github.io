package com.healerjean.proj.H02_Stream.H03_collect.H07_聚合计算;

import com.healerjean.proj.H02_Stream.H03_collect.H04_groupby分组.dto.Person;
import com.healerjean.proj.H02_Stream.H03_collect.H07_聚合计算.dto.SortEntry;
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
        max = list.stream().collect(Collectors.maxBy(Comparator.comparingInt(o -> o)));
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

    @Test
    public void sort() {
        List<Integer> list = Arrays.asList(1, 2, 4);

        //逆序  [4, 2, 1]
        Collections.reverse(list);
        System.out.println("逆序   " + list);

        //默认升序 [1, 2, 4]
        Collections.sort(list);
        Collections.sort(list, (o1, o2) -> o1 - o2);
        list.stream().sorted(Comparator.comparingInt(o -> o));
        System.out.println("默认升序" + list);

        //降序Collections.reverseOrder() [4, 2, 1]
        Collections.sort(list, Collections.reverseOrder());
        System.out.println("降序   " + list);



        //多条件排序
        List<SortEntry> sortEntries = new ArrayList<>();
        sortEntries.add(new SortEntry(23, 100));
        sortEntries.add(new SortEntry(27, 98));
        sortEntries.add(new SortEntry(29, 99));
        sortEntries.add(new SortEntry(29, 98));
        sortEntries.add(new SortEntry(22, 89));
        Collections.sort(sortEntries, (o1, o2) -> {
            int i = o1.getScore() - o2.getScore();  //先按照分数排序
            if (i == 0) {
                return o1.getAge() - o2.getAge();  //如果年龄相等了再用分数进行排序
            }
            return i;
        });
        System.out.println(sortEntries);

        //数组首个排序
        int[][] nums = {
                {1, 2},
                {3, 4},
                {2, 2}
        };
        Arrays.sort(nums, (o1, o2) -> o1[0] - o2[0]);
        Arrays.sort(nums, Comparator.comparingInt(o -> o[0]));


        //对象中子母排序
        List<SortEntry> sortEntries2 = new ArrayList<>();
        sortEntries2.add(new SortEntry("c"));
        sortEntries2.add(new SortEntry("a"));
        sortEntries2.add(new SortEntry("d"));
        sortEntries2.add(new SortEntry("A"));
        sortEntries2.add(new SortEntry("b"));
        // sortEntries2 = sortEntries2.stream().sorted(Comparator.comparing(sortEntry -> sortEntry.getName())).collect(Collectors.toList());
        Collections.sort(sortEntries2, Comparator.comparing(sortEntry -> sortEntry.getName()));
        System.out.println(sortEntries2);


    }

}
