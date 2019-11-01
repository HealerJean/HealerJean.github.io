/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.healerjean.proj.data.pojo.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhangyujin
 * @ClassName: SysUserInfo
 * @date 2099/1/1
 * @Description: SysUserInfo
 */
@Data
@Entity
@Table(name = "sys_user_info")
public class SysUserInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 用户名 */
	private String userName;
	/** 真实姓名 */
	private String realName;
	/** 邮箱 */
	private String email;
	/** 手机号 */
	private String telephone;
	/**盐*/
	private String salt ;
	/** 密码 */
	private String password;
	/** 用户类型（字典） */
	private String userType;
	/** 商品状态 */
	private String status;
	/** 创建人 */
	private Long createUser;
	/** 创建人名称 */
	private String createName;
	/** 创建时间 */
	private java.util.Date createTime;
	/** 更新人 */
	private Long updateUser;
	/** 更新人名称 */
	private String updateName;
	/** 更新时间 */
	private java.util.Date updateTime;

}
