/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.hlj.proj.data.pojo.demo;

import java.io.Serializable;

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
    private Long id;
    /**
     *
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     *
     */
    private Integer age;
    /**
     * 10可用，99删除
     */
    private String delFlag;
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
    @CreationTimestamp
    @Column(insertable = true, updatable = false)
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
    @UpdateTimestamp
    private java.util.Date updateTime;

}


/**
 create table `demo_entity` (
 `id` bigint(16) unsigned not null auto_increment,
 `name` varchar(64) not null,
 `phone` varchar(20) default '' comment '手机号',
 `email` varchar(64) default '' comment '邮箱',
 `age` int(100) default null,
 `del_flag` varchar(8) not null comment '10可用，99删除',
 `create_user` bigint(16) unsigned default null comment '创建人',
 `create_name` varchar(64) default '' comment '创建人名称',
 `create_time` timestamp not null default current_timestamp comment '创建时间',
 `update_user` bigint(16) unsigned default null comment '更新人',
 `update_name` varchar(64) default '' comment '更新人名称',
 `update_time` timestamp not null default current_timestamp on update current_timestamp comment '更新时间',
 primary key (`id`)
 ) engine=innodb default charset=utf8;
 */


