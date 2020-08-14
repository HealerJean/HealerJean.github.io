package com.healerjean.proj.H02_Stream.H01_获取Stream;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        //1、集合获取Stream
        Stream<Integer> stream = new ArrayList<Integer>().stream();

        //2、map获取Stream
        Stream<Integer> mapKeyStream = new HashMap<Integer, Integer>().keySet().stream();
        Stream<Integer> mapValueStream = new HashMap<Integer, Integer>().values().stream();
        Stream<Map.Entry<Integer, Integer>> mapEntryStream = new HashMap<Integer, Integer>().entrySet().stream();

        //3.1、数组获取Stream
        Stream<Integer> arrayStringStream1 = Arrays.stream(new Integer[3]);
        Stream<Integer> arrayStringStream2 = Stream.of(new Integer[3]);
        //3.2、基本类型获取数组
        IntStream intStream1 = Arrays.stream(new int[5]);
        Stream<int[]> intStream2 = Stream.of(new int[5]);
    }

    @Test
    public void test2() {
        //1、Integer集合转 int数组
        int[]  nums = new ArrayList<Integer>().stream().mapToInt(Integer::intValue).toArray();
        //2、int数组转Integer集合
        List<Integer> list = Arrays.stream(nums).mapToObj(Integer::new).collect(Collectors.toList());
    }
}
