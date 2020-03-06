/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.pojo.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhangyujin
 * @ClassName: SysTemplate
 * @date 2099/1/1
 * @Description: SysTemplate
 */
@Data
@Entity
@Table(name = "sys_template")
public class SysTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板内容
     */
    private String content;
    /**
     * 业务类型（邮箱，合同，短信等） 字典表
     */
    private String type;
    /**
     * 模板描述
     */
    private String description;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建人
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
