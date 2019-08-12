/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.flow;

import com.hlj.proj.data.common.query.PagingQuery;
import lombok.Data;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: FlowRefAuditorEventQuery
 * @date 2099/1/1
 * @Description: FlowRefAuditorEventQuery
 */
@Data
public class FlowRefAuditorEventQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public FlowRefAuditorEventQuery(){
		super(1, 10);
	}

	public FlowRefAuditorEventQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 审批记录表主键 */
	private Long refFlowAuditRecordId;
	/** 审核类型：角色或ID */
	private String auditType;
	/** 审核对象 */
	private Long auditObject;
	/** 创建人 */
	private Long createUser;
	/** 创建人名称 */
	private String createName;

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}

	/**
	 * 额外
	 */
	private List<Long> roleIds;



}
