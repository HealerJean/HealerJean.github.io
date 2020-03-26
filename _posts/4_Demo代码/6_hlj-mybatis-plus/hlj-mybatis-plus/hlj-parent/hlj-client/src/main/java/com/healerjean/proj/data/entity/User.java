package com.healerjean.proj.data.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author HealerJean
 * @ClassName User
 * @date 2020/3/5  16:11.
 * @Description
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    /** 添加日期进行测试 */
    private Date createDate;
    private Date createTime;
}
