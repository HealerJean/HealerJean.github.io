/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.demo;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 测试实体类;
 */
@Entity
@Table(name = "hlj_demo_entity")
@Data
@Accessors(chain = true)
public class DemoEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 主键 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**  */
	private String name;
	/** 手机号 */
	private String phone;
	/** 邮箱 */
	private String email;
	/**  */
	private Integer age;
	/** 10可用，99删除 */
	private String delFlag;
	/** 创建人 */
	private Long createUser;
	/** 创建人名称 */
	private String createName;
	/** 创建时间 */
	private LocalDateTime createTime;
	/** 更新人 */
	private Long updateUser;
	/** 更新人名称 */
	private String updateName;
	/** 更新时间 */
	private LocalDateTime updateTime;

}

/**


 CREATE TABLE `hlj_demo_entity` (
 `id` bigint(16) unsigned NOT NULL AUTO_INCREMENT,
 `name` varchar(64) NOT NULL,
 `phone` varchar(20) DEFAULT '' COMMENT '手机号',
 `email` varchar(64) DEFAULT '' COMMENT '邮箱',
 `age` int(100) DEFAULT NULL,
 `del_flag` varchar(8) NOT NULL COMMENT '10可用，99删除',
 `create_user` bigint(16) unsigned DEFAULT NULL COMMENT '创建人',
 `create_name` varchar(64) DEFAULT '' COMMENT '创建人名称',
 `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
 `update_user` bigint(16) unsigned DEFAULT NULL COMMENT '更新人',
 `update_name` varchar(64) DEFAULT '' COMMENT '更新人名称',
 `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
 PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=latin1


 */