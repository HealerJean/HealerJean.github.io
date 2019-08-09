/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.flow;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScfFlowAuditRecordTemp implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 流程实例流水号 */
	private String instantsNo;
	/** 流程步骤 */
	private Integer sept;
	/** 节点编码 */
	private String flowCode;
	/** 节点名称 */
	private String flowName;
	/** 节点编号 */
	private String nodeCode;
	/**
	 * 节点业务类型
	 */
	private String nodeServiceType;
	/** 节点名称 */
	private String nodeName;
	/** 审核步骤 */
	private Integer auditSept;
	/** 审核类型：角色或ID */
	private String auditType;
	/** 审核对象 */
	private Long auditObject;
	/** 审核数据序列化 */
	private String auditData;
	/** 状态 */
	private String status;
	/** 创建人 */
	private Long createUser;
	/** 创建人名称 */
	private String createName;
	/** 创建时间 */
	private java.util.Date createTime;

	/** 数量 */
	private Integer count;

}
