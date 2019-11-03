package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: SysDictionaryDataQuery
 * @date 2099/1/1
 * @Description: SysDictionaryDataQuery
 */
@Data
public class SysDictionaryDataQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public SysDictionaryDataQuery(){
		super(1, 10);
	}

	public SysDictionaryDataQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 数据类型type_key  表dictionary_type */
	private String dataKey;
	/** 字典数据 描述 */
	private String dataValue;
	/** 数据类型type_key  表dictionary_type */
	private String refTypeKey;
	/** 排序 */
	private Integer sort;
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
