package com.healerjean.proj.H02_Stream.H02_简单属性.H12_mapToObj;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TestMain
 *
 * @author zhangyujin
 * @date 2024/8/12
 */
public class TestMain {


    @Test
    public void test(){
        String array[] = {"1","2","3"};
        Integer [] ints = Arrays.stream(array).mapToInt(Integer::intValue).toArray();


    }
}
