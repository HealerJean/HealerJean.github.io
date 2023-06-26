package com.healerjean.proj.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangyujin
 * @date 2021/12/17  3:46 下午.
 * @description
 */
@Data
public class User {
    private String uid;
    private String name;
    private Integer age;
    private Date birthday;

}
