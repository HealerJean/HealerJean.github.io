/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhangyujin
 * @ClassName: SysEmailLog
 * @date 2099/1/1 sys_email_log
 * @Description: SysEmailLog
 */
@Data
@Entity
@Table(name = "sys_email_log")
public class SysEmailLog implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
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
	/** 创建时间 */
	private java.util.Date createTime;
	/** 更新时间 */
	private java.util.Date updateTime;

}
