package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: SysUserInfoQuery
 * @date 2099/1/1
 * @Description: SysUserInfoQuery
 */
@Data
public class SysUserInfoQuery extends PagingQuery {
	private static final long serialVersionUID = 1L;

	public SysUserInfoQuery(){
		super(1, 10);
	}

	public SysUserInfoQuery(int pageNo, int pageSize){
		super(pageNo, pageSize);
	}

	/** 淘宝联盟阿里妈妈账号主键 */
	private Long refAlimamaInfoId;
	/** 用户名 */
	private String userName;
	/** 真实姓名 */
	private String realName;
	/** 邮箱 */
	private String email;
	/** 手机号 */
	private String telephone;
	/** 密码 */
	private String password;
	/**密码随机盐*/
	private String salt;
	/** 用户类型（字典） */
	private String userType;
	/** 商品状态 */
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
