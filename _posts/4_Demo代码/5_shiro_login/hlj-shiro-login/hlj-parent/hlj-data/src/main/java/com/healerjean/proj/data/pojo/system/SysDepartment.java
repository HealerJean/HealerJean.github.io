/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.pojo.system;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: SysDepartment
 * @date 2099/1/1
 * @Description: SysDepartment
 */
@Data
public class SysDepartment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门描述
     */
    private String description;
    /**
     * 父部门
     */
    private Long pid;
    /**
     * 状态: 10：有效，99：无效
     */
    private String status;
    /**
     * 创建人id
     */
    private Long createUser;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建时间
     */
    private java.util.Date createTime;
    /**
     * 更新人
     */
    private Long updateUser;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新时间
     */
    private java.util.Date updateTime;

}
