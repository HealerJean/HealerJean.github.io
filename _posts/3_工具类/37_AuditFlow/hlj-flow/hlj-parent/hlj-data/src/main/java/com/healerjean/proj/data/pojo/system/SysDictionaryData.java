/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhangyujin
 * @ClassName: SysDictionaryData
 * @date 2099/1/1
 * @Description: SysDictionaryData
 */
@Data
@Entity
@Table(name = "sys_dictionary_data")
public class SysDictionaryData implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 数据类型type_key  表dictionary_type */
	private String dataKey;
	/** 字典数据 描述 */
	private String dataValue;
	/** 数据类型type_key  表dictionary_type */
	private String refTypeKey;
	/** 排序 */
	private Integer sort;
	/** 状态 */
	private String status;
	/** 创建人 */
	private Long createUser;
	/** 创建人名称 */
	private String createName;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 更新人 */
	private Long updateUser;
	/** 更新人名称 */
	private String updateName;
	/** 更新时间 */
	private java.util.Date updateTime;

}
