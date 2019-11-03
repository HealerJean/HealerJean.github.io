package com.healerjean.proj.data.pojo.flow;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowAuditDefaultUserQuery
 * @date 2099/1/1
 * @Description: FlowAuditDefaultUserQuery
 */
@Data
public class FlowAuditDefaultUserQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public FlowAuditDefaultUserQuery(){
		super(1, 10);
	}

	public FlowAuditDefaultUserQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 任务类型 */
	private String taskType;
	/** 任务名字 */
	private String taskName;
	/** 审批类型属性：抄送人、审批人类型 */
	private String auditUserType;
	/** 审批对象类型：角色或ID */
	private String auditObjectType;
	/** 审批对象对应的id */
	private Long auditObjectId;
	/** 处理顺序，抄送人0 */
	private Integer step;
	/** 状态 */
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
