/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.demo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

/**
 * @author zhangyujin
 * @ClassName: DemoEntity
 * @date 2099/1/1
 * @Description: DemoEntity
 */
@Entity
@Table(name = "demo_entity")
@Accessors(chain = true)
@Data
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
	@CreationTimestamp
	@Column(insertable = true,updatable = false)
	private java.util.Date createTime;
	/** 更新人 */
	private Long updateUser;
	/** 更新人名称 */
	private String updateName;
	/** 更新时间 */
	@UpdateTimestamp
	private java.util.Date updateTime;
	/**  */
	private Integer newColumn;

}