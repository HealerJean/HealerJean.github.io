/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.pojo.demo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

/**
 * 测试实体类;
 */
@Entity
@Table(name = "demo_entity")
@Data
@Accessors(chain = true)
public class DemoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** mybatis-plus如果希望使用数据库自增 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /** 姓名 */
    private String name;
    /** 手机号  */
    private String phone;
    /**  邮箱 */
    private String email;
    /** 年龄  */
    private Integer age;
    /**  10可用，99删除  */
    private String status;
    /** 创建人 */
    private Long createUser;
    /** 创建人名称  */
    private String createName;
    /**  创建时间 */
    @CreationTimestamp
    @Column(insertable = true, updatable = false)
    private java.util.Date createTime;
    /**  更新人 */
    private Long updateUser;
    /** 更新人名称 */
    private String updateName;
    /**  更新时间 */
    @UpdateTimestamp
    private java.util.Date updateTime;

}



    // CREATE TABLE `demo_entity` (
    //     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    //     `name` varchar(64) NOT NULL,
    //     `phone` varchar(20) DEFAULT '' COMMENT '手机号',
    //     `email` varchar(64) DEFAULT '' COMMENT '邮箱',
    //     `age` int(10) DEFAULT NULL,
    //     `status` varchar(8) NOT NULL COMMENT '状态',
    //     `create_user` bigint(16) unsigned DEFAULT NULL COMMENT '创建人',
    //     `create_name` varchar(64) DEFAULT '' COMMENT '创建人名称',
    //     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    //     `update_user` bigint(16) unsigned DEFAULT NULL COMMENT '更新人',
    //     `update_name` varchar(64) DEFAULT '' COMMENT '更新人名称',
    //     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    //     PRIMARY KEY (`id`)
    //     ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


