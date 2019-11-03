/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.flow;

import java.io.Serializable;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowWorkRecord
 * @date 2099/1/1
 * @Description: FlowWorkRecord
 */
@Data
public class FlowWorkRecord implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 流程实例流水号 */
	private String instantsNo;
	/** 工作流节点编码 */
	private String flowCode;
	/** 工作流节点名称 */
	private String flowName;
	/** 流程步骤 */
	private Integer step;
	/** 节点编号 */
	private String nodeCode;
	/** 节点名称 */
	private String nodeName;
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
