package com.healerjean.proj.data.pojo.flow;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowWorkRecordQuery
 * @date 2099/1/1
 * @Description: FlowWorkRecordQuery
 */
@Data
public class FlowWorkRecordQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public FlowWorkRecordQuery(){
		super(1, 10);
	}

	public FlowWorkRecordQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

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

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}



}
