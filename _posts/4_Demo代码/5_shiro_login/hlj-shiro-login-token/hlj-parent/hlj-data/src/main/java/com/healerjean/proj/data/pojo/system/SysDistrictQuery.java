package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: SysDistrictQuery
 * @date 2099/1/1
 * @Description: SysDistrictQuery
 */
@Data
public class SysDistrictQuery extends PagingQuery {
    private static final long serialVersionUID = 1L;

    public SysDistrictQuery() {
        super(1, 10);
    }

    public SysDistrictQuery(int pageNo, int pageSize) {
        super(pageNo, pageSize);
    }

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
     * 计算总记录数
     */
    public int calcItemCount(Object t) {
        return 0;
    }


}
