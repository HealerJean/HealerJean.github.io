/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.user;

import com.hlj.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: TeventQuery
 * @date 2099/1/1
 * @Description: TeventQuery
 */
@Data
public class TeventQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public TeventQuery(){
		super(1, 10);
	}

	public TeventQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 事件的类型：比如新增用户、新增积分 */
	private String type;
	/** 事件进行到的环节：比如，新建、已发布、已处理 */
	private String process;
	/** 事件的内容，用于保存事件发生时需要传递的数据 */
	private String content;

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}



}
