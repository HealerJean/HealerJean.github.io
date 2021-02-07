/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.hlj.proj.data.pojo.user;

import java.io.Serializable;

import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: Tuser
 * @date 2099/1/1
 * @Description: Tuser
 */
@Data
public class Tuser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;

}
