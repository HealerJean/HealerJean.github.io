package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: SysUserDepartmentRefQuery
 * @date 2099/1/1
 * @Description: SysUserDepartmentRefQuery
 */
@Data
public class SysUserDepartmentRefQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public SysUserDepartmentRefQuery(){
		super(1, 10);
	}

	public SysUserDepartmentRefQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 用户id */
	private Long refUserId;
	/** 部门Id */
	private Long refDepartmentId;
	/** 状态: 10：有效，99：无效 */
	private String status;

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}



}
