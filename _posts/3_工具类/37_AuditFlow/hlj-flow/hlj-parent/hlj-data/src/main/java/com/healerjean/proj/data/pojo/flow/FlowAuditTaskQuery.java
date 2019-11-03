package com.healerjean.proj.data.pojo.flow;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowAuditTaskQuery
 * @date 2099/1/1
 * @Description: FlowAuditTaskQuery
 */
@Data
public class FlowAuditTaskQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public FlowAuditTaskQuery(){
		super(1, 10);
	}

	public FlowAuditTaskQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 任务类型 */
	private String taskType;
	/** 任务名字 */
	private String taskName;
	/** 任务数据Json格式 */
	private String taskData;
	/** 审批状态 */
	private String status;
	/** 创建人,发起人 */
	private Long createUser;
	/** 创建人名称 */
	private String createName;
	/** 更新人 */
	private Long updateUser;
	/** 更新人名称 */
	private String updateName;

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}



}
