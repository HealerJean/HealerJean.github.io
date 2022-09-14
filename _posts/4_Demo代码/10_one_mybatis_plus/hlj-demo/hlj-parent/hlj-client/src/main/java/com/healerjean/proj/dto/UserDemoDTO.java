package com.healerjean.proj.dto;

import lombok.Data;

@Data
public class UserDemoDTO {


    /**
     * 主键
     */
    private Long id;
    /**
     * 名字
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 电话
     */
    private String telPhone;
    /**
     * 邮箱
     */
    private String email;

}
