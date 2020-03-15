package com.healerjean.proj.data.pojo.system;/*
 * Copyright (C) 2019 xiaomi.com, Inc. All Rights Reserved.
 */

import com.healerjean.proj.data.common.query.PagingQuery;
import lombok.Data;

/**
 * @author zhangyujin
 * @ClassName: SysTemplateQuery
 * @date 2099/1/1
 * @Description: SysTemplateQuery
 */
@Data
public class SysTemplateQuery extends PagingQuery {
    private static final long serialVersionUID = 1L;

    public SysTemplateQuery() {
        super(1, 10);
    }

    public SysTemplateQuery(int pageNo, int pageSize) {
        super(pageNo, pageSize);
    }

    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板内容
     */
    private String content;
    /**
     * 业务类型（邮箱，合同，短信等） 字典表
     */
    private String type;
    /**
     * 模板描述
     */
    private String description;
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
