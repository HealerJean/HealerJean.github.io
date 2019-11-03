/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.flow;

import java.io.Serializable;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowAuditUserDetail
 * @date 2099/1/1
 * @Description: FlowAuditUserDetail
 */
@Data
public class FlowAuditUserDetail implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
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
	/** 创建时间 */
	private java.util.Date createTime;
	/** 更新人 */
	private Long updateUser;
	/** 更新人名称 */
	private String updateName;
	/** 更新时间 */
	private java.util.Date updateTime;

}
