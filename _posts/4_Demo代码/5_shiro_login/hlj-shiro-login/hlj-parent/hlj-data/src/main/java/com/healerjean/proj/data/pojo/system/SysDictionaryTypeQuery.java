package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: SysDictionaryTypeQuery
 * @date 2099/1/1
 * @Description: SysDictionaryTypeQuery
 */
@Data
public class SysDictionaryTypeQuery extends PagingQuery {
    private static final long serialVersionUID = 1L;

    public SysDictionaryTypeQuery() {
        super(1, 10);
    }

    public SysDictionaryTypeQuery(int pageNo, int pageSize) {
        super(pageNo, pageSize);
    }

    /**
     * 字典类型
     */
    private String typeKey;
    /**
     * 字典类型 描述
     */
    private String typeDesc;
    /**
     * 状态
     */
    private String status;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 更新人
     */
    private Long updateUser;
    /**
     * 更新人名称
     */
    private String updateName;

    /**
     * 计算总记录数
     */
    public int calcItemCount(Object t) {
        return 0;
    }


}
