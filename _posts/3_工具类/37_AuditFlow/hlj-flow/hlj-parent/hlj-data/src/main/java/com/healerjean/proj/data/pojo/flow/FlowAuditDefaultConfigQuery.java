package com.healerjean.proj.data.pojo.flow;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowAuditDefaultConfigQuery
 * @date 2099/1/1
 * @Description: FlowAuditDefaultConfigQuery
 */
@Data
public class FlowAuditDefaultConfigQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public FlowAuditDefaultConfigQuery(){
		super(1, 10);
	}

	public FlowAuditDefaultConfigQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

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
	/** 更新人 */
	private Long updateUser;
	/** 更新人名称 */
	private String updateName;

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}



}
