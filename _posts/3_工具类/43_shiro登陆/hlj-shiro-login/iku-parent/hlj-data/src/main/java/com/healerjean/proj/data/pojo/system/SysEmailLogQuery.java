package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: SysEmailLogQuery
 * @date 2099/1/1
 * @Description: SysEmailLogQuery
 */
@Data
public class SysEmailLogQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public SysEmailLogQuery(){
		super(1, 10);
	}

	public SysEmailLogQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 邮件类型 数据字典 */
	private String type;
	/** 邮件标题 */
	private String subject;
	/** 邮件内容 */
	private String content;
	/** 发送邮箱 */
	private String sendEmail;
	/** 接收邮箱 */
	private String receiveMails;
	/** success 发送成功 ，error 发送失败  */
	private String status;
	/** 发送状态信息 发送成功、异常信息 */
	private String msg;

	/** 计算总记录数 */
	public int calcItemCount(Object t) {
		return 0;
	}



}
