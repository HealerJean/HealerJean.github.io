/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.flow;

import lombok.Data;

import java.io.Serializable;
/**
 * @author zhangyujin
 * @ClassName: FlowAuditTask
 * @date 2099/1/1
 * @Description: FlowAuditTask
 */
@Data
public class FlowAuditTask implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 任务类型 */
	private String taskType;
	/** 任务名字 */
	private String taskName;
	/** 任务数据Json格式 */
	private String taskData;
	/** 审批状态 */
	private String status;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 创建人,发起人 */
	private Long createUser;
	/** 创建人名称 */
	private String createName;
	/** 更新人 */
	private Long updateUser;
	/** 更新人名称 */
	private String updateName;
	/** 更新时间 */
	private java.util.Date updateTime;

}
