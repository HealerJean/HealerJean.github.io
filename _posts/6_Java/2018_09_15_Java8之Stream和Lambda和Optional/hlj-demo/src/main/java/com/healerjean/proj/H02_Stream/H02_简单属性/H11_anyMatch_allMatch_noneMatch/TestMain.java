package com.healerjean.proj.H02_Stream.H12_anyMatch_allMatch_noneMatch;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        List<String> list = Arrays.asList("张三丰", "张无忌", "杨过");
        boolean allMatch = list.stream().allMatch(str -> "张三丰".equals(str));
        System.out.println(allMatch);//false

        boolean noneMatch = list.stream().noneMatch(str -> "张三丰".equals(str));
        System.out.println(noneMatch);//false

        boolean anyMatch = list.stream().anyMatch(str -> "张三丰".equals(str));
        System.out.println(anyMatch);//true

    }

}
