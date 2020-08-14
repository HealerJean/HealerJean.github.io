package com.healerjean.proj.H02_Stream.H03_collect.H03_收集并映射;

import com.healerjean.proj.H02_Stream.H03_collect.H03_收集并映射.dto.Person;
import org.junit.Test;

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
     * 收集并映射
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
