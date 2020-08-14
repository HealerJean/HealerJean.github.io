package com.healerjean.proj.H02_Stream.H03_collect.H04_groupby分组.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author HealerJean
 * @ClassName Person
 * @date 2020/8/13  19:47.
 * @Description
 */
@Data
public class Person {

    private Long id;
    private String name;
    private Map<String, Integer> scores;

    public Person() {
    }

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

