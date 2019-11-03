package com.healerjean.proj.data.pojo.flow;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowWorkDefinitionQuery
 * @date 2099/1/1
 * @Description: FlowWorkDefinitionQuery
 */
@Data
public class FlowWorkDefinitionQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public FlowWorkDefinitionQuery(){
		super(1, 10);
	}

	public FlowWorkDefinitionQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 流程实例流水号 */
	private String instantsNo;
	/** 工作流编码 */
	private String flowCode;
	/** 工作流名称 */
	private String flowName;
	/** 节点顺序流程 */
	private String flowDefinition;
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



}
