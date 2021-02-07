/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.user;

import com.hlj.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: TuserQuery
 * @date 2099/1/1
 * @Description: TuserQuery
 */
@Data
public class TuserQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public TuserQuery(){
		super(1, 10);
	}

	public TuserQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 用户名 */
	private String userName;

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}



}
