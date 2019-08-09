/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.flow;

import com.hlj.proj.data.common.query.PagingQuery;
import lombok.Data;

import java.util.List;


@Data
public class ScfFlowAuditRecordTempQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public ScfFlowAuditRecordTempQuery(){
		super(1, 10);
	}

	public ScfFlowAuditRecordTempQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

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

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}

	/** 审批角色 */
	private List<Long> roles;

	/** 审批人 */
	private Long userId;

}
