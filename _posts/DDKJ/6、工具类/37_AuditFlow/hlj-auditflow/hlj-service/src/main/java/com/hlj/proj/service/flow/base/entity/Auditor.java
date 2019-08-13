package com.hlj.proj.service.flow.base.entity;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Auditor
 * @Author TD
 * @Date 2019/6/12 20:47
 * @Description 审批人
 */
@Data
public class Auditor {


//审批的种类有两种，通过角色，通过角色和用户ID来进行审批定位、

    /** 角色集合 */
    private List<Long> roles;
    /** 用户ID集合 */
    private List<Long> ids;
    /** 该节点审批状态 */
    private String status;
}

