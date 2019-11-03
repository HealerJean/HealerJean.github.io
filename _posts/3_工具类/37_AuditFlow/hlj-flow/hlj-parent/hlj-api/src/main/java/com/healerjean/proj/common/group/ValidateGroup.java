package com.healerjean.proj.common.group;

/**
 * @author HealerJean
 * @version 1.0v
 * @Description
 * @ClassName CoreValidateGroup
 * @date 2019/4/17  9:30.
 */
public interface ValidateGroup {

    /** 用户注册 */
     interface RegisterUser {  }
     interface ManageAddUser {  }

    /** 用户登陆 */
     interface Login { }

    /** 添加菜单 */
     interface AddMenu { }
    /** 添加菜单  */
     interface UpdateMenu { }
    /**  添加角色 */
     interface AddRole {  }
    /** 更新角色  */
     interface UpdateRole {  }
    /** 添加部门校验接口 */
     interface AddDepartment{}
    /** 修改部门校验接口 */
     interface UpdateDepartment{}

     interface AddDictType{}
     interface UpdateDictType{}
     interface AddDictData{}
     interface UpdateDictData{}


     /** 配置默认审批用户*/
     interface ConfigDefultAuditUser {}
    /** 发起审批申请*/
    interface TaskAuditApply{}
    /** 审批成功*/
    interface AuditTask {}


}
