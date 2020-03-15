package com.healerjean.proj.data.pojo.system;
/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: SysUserRoleRefQuery
 * @date 2099/1/1
 * @Description: SysUserRoleRefQuery
 */
@Data
public class SysUserRoleRefQuery extends PagingQuery {
    private static final long serialVersionUID = 1L;

    public SysUserRoleRefQuery() {
        super(1, 10);
    }

    public SysUserRoleRefQuery(int pageNo, int pageSize) {
        super(pageNo, pageSize);
    }

    /**
     * 用户ID
     */
    private Long refUserId;
    /**
     * 角色ID
     */
    private Long refRoleId;
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
