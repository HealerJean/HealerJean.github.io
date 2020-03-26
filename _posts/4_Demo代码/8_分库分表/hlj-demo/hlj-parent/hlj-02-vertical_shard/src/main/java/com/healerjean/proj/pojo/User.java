/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    /** mybatis-plus如果希望使用数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String city;
}






