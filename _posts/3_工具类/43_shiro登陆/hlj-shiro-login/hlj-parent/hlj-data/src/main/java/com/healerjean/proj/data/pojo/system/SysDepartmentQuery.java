package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: SysDepartmentQuery
 * @date 2099/1/1
 * @Description: SysDepartmentQuery
 */
@Data
public class SysDepartmentQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public SysDepartmentQuery(){
		super(1, 10);
	}

	public SysDepartmentQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 部门名称 */
	private String name;
	/** 部门描述 */
	private String description;
	/** 父部门 */
	private Long pid;
	/** 状态: 10：有效，99：无效 */
	private String status;
	/** 创建人id */
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
