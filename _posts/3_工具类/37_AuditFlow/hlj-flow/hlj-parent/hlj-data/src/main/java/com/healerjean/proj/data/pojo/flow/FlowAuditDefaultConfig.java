/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.flow;

import java.io.Serializable;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowAuditDefaultConfig
 * @date 2099/1/1
 * @Description: FlowAuditDefaultConfig
 */
@Data
public class FlowAuditDefaultConfig implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 任务类型 */
	private String taskType;
	/** 任务名字 */
	private String taskName;
	/** 是否可以更改默认审批人 */
	private Boolean modify;
	/** 状态10有效 99废弃 */
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
