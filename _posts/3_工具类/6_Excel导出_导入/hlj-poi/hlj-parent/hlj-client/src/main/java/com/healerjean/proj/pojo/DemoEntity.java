/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
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
public class DemoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 姓名 */
    @ExcelProperty("name")
    private String name;
    /** 手机号  */
    @ExcelProperty("phone")
    private String phone;
    /**  邮箱 */
    @ExcelProperty("email")
    private String email;
    /** 年龄  */
    private Integer age;
    /**  10可用，99删除  */
    private String status;
    /** 创建人 */
    private Long createUser;
    /** 创建人名称  */
    @ExcelProperty("createName")
    private String createName;
    /**  创建时间 */
    private java.util.Date createTime;
    /**  更新人 */
    private Long updateUser;
    /** 更新人名称 */
    private String updateName;
    /**  更新时间 */
    private java.util.Date updateTime;

}





