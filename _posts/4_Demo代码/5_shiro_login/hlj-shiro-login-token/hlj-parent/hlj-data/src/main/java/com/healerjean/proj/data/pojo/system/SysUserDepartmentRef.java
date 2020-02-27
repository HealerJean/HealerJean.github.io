/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.system;

import java.io.Serializable;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: SysUserDepartmentRef
 * @date 2099/1/1
 * @Description: SysUserDepartmentRef
 */
@Data
public class SysUserDepartmentRef implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 用户id */
	private Long refUserId;
	/** 部门Id */
	private Long refDepartmentId;
	/** 状态: 10：有效，99：无效 */
	private String status;
	/** 更新时间 */
	private java.util.Date updateTime;

}
