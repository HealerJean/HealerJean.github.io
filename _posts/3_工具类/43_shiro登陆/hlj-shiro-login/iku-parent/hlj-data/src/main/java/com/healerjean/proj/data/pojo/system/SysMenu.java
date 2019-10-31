/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhangyujin
 * @ClassName: SysMenu
 * @date 2099/1/1
 * @Description: SysMenu
 */
@Data
@Entity
@Table(name = "sys_menu")
public class SysMenu implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 系统CODE */
	private String refSystemCode;
	/** 菜单名称 */
	private String menuName;
	/** 菜单调用地址 */
	private String url;
	/** 调用方法（GET，POST，PUT，DELETE） */
	private String method;
	/** 状态 */
	private String status;
	/** 父级id */
	private Long pid;
	/** 父链id，“,”分隔 */
	private String pchain;
	/** 描述 */
	private String description;
	/** 显示排序 */
	private Integer sort;
	/** 菜单图标 */
	private String icon;
	/** 前端菜单标识（前端菜单唯一标识） */
	private String frontKey;
	/** 是否需要权限拦截，10：需要，99：不需要 */
	private String isPermission;
	/** 菜单类型：0: 后端路径, 1:前端菜单，2:非展示前端菜单 */
	private String menuType;
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
