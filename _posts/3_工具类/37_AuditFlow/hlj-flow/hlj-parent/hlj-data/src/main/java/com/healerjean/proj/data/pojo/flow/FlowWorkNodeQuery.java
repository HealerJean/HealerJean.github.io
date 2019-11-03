package com.healerjean.proj.data.pojo.flow;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: FlowWorkNodeQuery
 * @date 2099/1/1
 * @Description: FlowWorkNodeQuery
 */
@Data
public class FlowWorkNodeQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public FlowWorkNodeQuery(){
		super(1, 10);
	}

	public FlowWorkNodeQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 节点编号 */
	private String nodeCode;
	/** 节点名称 */
	private String nodeName;
	/** 流程节点或者审核节点 */
	private String nodeType;
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
