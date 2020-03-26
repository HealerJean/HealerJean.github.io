/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.pojo.demo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
}






