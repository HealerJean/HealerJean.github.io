package com.healerjean.proj.H02_Stream.H02_简单属性.H10_findAny_findFirst;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2020/8/13  15:01.
 * @Description
 */

public class TestMain {

    @Test
    public void test() {
        //findAny：能够从流中随便选一个元素出来，它返回一个Optional类型的元素。
        List<String> list = Arrays.asList("张三丰", "张无忌", "杨过");
        Optional<String> strOptional = list.stream().findAny();
        System.out.println(strOptional.get());

        //findFirst：找出第一个。它返回一个Optional类型的元素。
        Optional<String> first = list.stream().findFirst();
        System.out.println(first.get());
    }

}
