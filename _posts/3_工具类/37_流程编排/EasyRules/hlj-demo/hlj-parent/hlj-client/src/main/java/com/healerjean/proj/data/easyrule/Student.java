package com.healerjean.proj.data.easyrule;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Student
 *
 * @author zhangyujin
 * @date 2023/11/4
 */
@Accessors(chain = true)
@Data
public class Student {

    /**
     * 年级
     */
    private Integer grade;
    /**
     * 性别
     */
    private String gender;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 是否强壮
     */
    private Boolean isStrong;
    /**
     * 身高
     */
    private Integer height;
    /**
     * 是否一个好苗子
     */
    private Boolean isGoodSeed = true;
}
