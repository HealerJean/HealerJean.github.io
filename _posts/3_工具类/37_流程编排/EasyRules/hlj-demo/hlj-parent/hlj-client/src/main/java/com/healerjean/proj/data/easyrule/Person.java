package com.healerjean.proj.data.easyrule;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Person
 *
 * @author zhangyujin
 * @date 2023/11/4
 */
@Accessors(chain = true)
@Data
public class Person {

    /**
     * 名字
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别
     */
    private String gender;

}
