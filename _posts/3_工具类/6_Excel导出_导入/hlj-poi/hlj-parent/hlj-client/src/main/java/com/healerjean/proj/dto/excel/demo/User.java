package com.healerjean.proj.dto.excel.demo;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangyujin
 * @date 2021/12/20  8:02 下午.
 * @description
 */
@Data
public class User {
    private String uid;
    private String name;
    private Integer age;
    private Date birthday;
}
