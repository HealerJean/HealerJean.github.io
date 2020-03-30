/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试实体类;
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键  */
    private Long id;
    private String name;
    private String city;
    private Integer age;
    private String status;
    private Date createTime;
    private Date updateTime;
}






