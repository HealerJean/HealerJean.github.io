package com.healerjean.proj.H02_Stream.H03_collect.H07_聚合计算.dto;

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

