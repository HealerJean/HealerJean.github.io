package com.healerjean.proj.data.pojo.flow;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowAuditUserDetailQuery
 * @date 2099/1/1
 * @Description: FlowAuditUserDetailQuery
 */
@Data
public class FlowAuditUserDetailQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public FlowAuditUserDetailQuery(){
		super(1, 10);
	}

	public FlowAuditUserDetailQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 审批任务Id */
	private Long refAuditTaskId;
	/** 任务类型 */
	private String taskType;
	/** 任务名字 */
	private String taskName;
	/** 审批到了第几步，抄送人0 */
	private Integer step;
	/** 抄送人、审批人类型 */
	private String auditUserType;
	/** 审批对象类型：角色或ID */
	private String auditObjectType;
	/** 审批对象对应的id */
	private Long auditObjectId;
	/** 审批状态 */
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
