/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.flow;

import lombok.Data;

import java.io.Serializable;
/**
 * @author zhangyujin
 * @ClassName: FlowWorkDefinition
 * @date 2099/1/1
 * @Description: FlowWorkDefinition
 */
@Data
public class FlowWorkDefinition implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 流程实例流水号 */
	private String instantsNo;
	/** 工作流编码 */
	private String flowCode;
	/** 工作流名称 */
	private String flowName;
	/** 节点顺序流程 */
	private String flowDefinition;
	/** 状态 */
	private String status;
	/** 创建人 */
	private Long createUser;
	/** 创建人名称 */
	private String createName;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 更新时间 */
	private java.util.Date updateTime;

}
