/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */package com.hlj.proj.data.pojo.user;

import java.io.Serializable;
import lombok.Data;
/**
 * @author zhangyujin
 * @ClassName: Tpoint
 * @date 2099/1/1
 * @Description: Tpoint
 */
@Data
public class Tpoint implements Serializable{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 关联的用户id */
	private Long userId;
	/** 积分金额 */
	private java.math.BigDecimal amount;

}
