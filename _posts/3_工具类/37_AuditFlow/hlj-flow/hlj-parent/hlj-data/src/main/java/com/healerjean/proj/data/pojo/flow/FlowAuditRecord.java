/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.flow;

import java.io.Serializable;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowAuditRecord
 * @date 2099/1/1
 * @Description: FlowAuditRecord
 */
@Data
public class FlowAuditRecord implements Serializable{
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
	/** 审批意见 */
	private String auditMessage;
	/** 审批附件Josn集合 */
	private String refFileIds;
	/** 抄送人、审批人类型 */
	private String auditUserType;
	/** 审批对象类型：角色或ID */
	private String optUserType;
	/** 审批对象对应的id */
	private Long auditObjectId;
	/** 审批人Id */
	private Long optUserId;
	/** 审批人名字 */
	private String optUserName;
	/** 审批状态 */
	private String status;
	/** 创建时间 */
	private java.util.Date createTime;

}
