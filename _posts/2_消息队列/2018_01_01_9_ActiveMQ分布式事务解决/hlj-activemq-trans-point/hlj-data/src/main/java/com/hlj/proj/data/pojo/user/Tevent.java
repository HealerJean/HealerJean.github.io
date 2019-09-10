/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.user;

import java.io.Serializable;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: Tevent
 * @date 2099/1/1
 * @Description: Tevent
 */
@Data
public class Tevent implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 事件的类型：比如新增用户、新增积分 */
	private String type;
	/** 事件进行到的环节：比如，新建、已发布、已处理 */
	private String process;
	/** 事件的内容，用于保存事件发生时需要传递的数据 */
	private String content;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 更新时间 */
	private java.util.Date updateTime;

}
