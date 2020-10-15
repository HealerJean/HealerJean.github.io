package com.healerjean.proj.H02_Stream.H03_collect.H07_聚合计算.dto;

import lombok.Data;

/**
 * @author HealerJean
 * @ClassName SortEntry
 * @date 2020/8/14  14:09.
 * @Description
 */
@Data
public class SortEntry {

    private Integer age ;
    private Integer score;
    private String name ;

    public SortEntry(Integer age, Integer score) {
        this.age = age;
        this.score = score;
    }

    public SortEntry( String name) {
        this.name = name;
    }
}
