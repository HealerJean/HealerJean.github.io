/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */
package com.healerjean.proj.data.pojo.system;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangyujin
 * @ClassName: SysDistrict
 * @date 2099/1/1
 * @Description: SysDistrict
 */
@Data
public class SysDistrict implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 省-编码
     */
    private String provinceCode;
    /**
     * 省-名称
     */
    private String provinceName;
    /**
     * 城市-编码
     */
    private String cityCode;
    /**
     * 城市-名称
     */
    private String cityName;
    /**
     * 区/县-编码
     */
    private String districtCode;
    /**
     * 区/县-名称
     */
    private String districtName;
    /**
     * 状态: 10：有效，99：无效
     */
    private String status;
    /**
     * 更新时间
     */
    private java.util.Date updateTime;

}
