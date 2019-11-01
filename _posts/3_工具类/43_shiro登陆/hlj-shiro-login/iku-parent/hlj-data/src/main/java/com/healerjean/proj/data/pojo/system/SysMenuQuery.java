package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: SysMenuQuery
 * @date 2099/1/1
 * @Description: SysMenuQuery
 */
@Data
public class SysMenuQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public SysMenuQuery(){
		super(1, 10);
	}

	public SysMenuQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 系统CODE */
	private String refSystemCode;
	/** 菜单名称 */
	private String menuName;
	/** 菜单调用地址 */
	private String url;
	/** 调用方法（GET，POST，PUT，DELETE） */
	private String method;
	/** 状态 */
	private String status;
	/** 父级id */
	private Long pid;
	/** 父链id，“,”分隔 */
	private String pchain;
	/** 描述 */
	private String description;
	/** 显示排序 */
	private Integer sort;
	/** 菜单图标 */
	private String icon;
	/** 前端菜单标识（前端菜单唯一标识） */
	private String frontKey;
	/** 是否需要权限拦截，10：需要，99：不需要 */
	private String isPermission;
	/** 菜单类型：0: 后端路径, 1:前端菜单，2:非展示前端菜单 */
	private String menuType;
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


	/**
	 * 角色Id
	 */
	private Long refRoleId ;

	/** 角色ID */
	private List<Long> refRoleIds;

}
