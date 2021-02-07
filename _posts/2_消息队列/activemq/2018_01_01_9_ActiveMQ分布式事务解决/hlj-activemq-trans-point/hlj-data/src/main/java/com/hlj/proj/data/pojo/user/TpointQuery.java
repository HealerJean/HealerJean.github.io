/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.user;

import com.hlj.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: TpointQuery
 * @date 2099/1/1
 * @Description: TpointQuery
 */
@Data
public class TpointQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public TpointQuery(){
		super(1, 10);
	}

	public TpointQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 关联的用户id */
	private Long userId;
	/** 积分金额 */
	private java.math.BigDecimal amount;

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}



}
