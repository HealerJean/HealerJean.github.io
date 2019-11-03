/*
 * Powered By code-generator
 */
package com.healerjean.proj.api.system;


import com.healerjean.proj.common.page.PageDTO;
import com.healerjean.proj.dto.system.MenuDTO;
import com.healerjean.proj.dto.system.RoleDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;

import java.util.List;


public interface RoleService {


    /**
     * 新增角色
     */
    void addRole(RoleDTO roleDTO, LoginUserDTO loginUserDTO);

    /**
     * 角色列表查询
     */
    PageDTO<RoleDTO> getRoles(RoleDTO dto);

    /**
     * 角色列表查询，无分页
     */
    List<RoleDTO> getRolesNoPage(RoleDTO roleDTO);

    /**
     * 角色查询
     */
    RoleDTO getRole(Long roleId);

    /**
     * 角色修改
     */
    void updateRole(RoleDTO roleDTO, LoginUserDTO loginUserDTO);

    /**
     * 角色删除
     */
    void deleteRole(Long roleId, LoginUserDTO loginUserDTO);

    /**
     * 角色权限查询
     */
    List<MenuDTO> getMenusByRole(Long roleId, MenuDTO dto);

    /**
     * 角色权限修改
     */
    void updateMenusByRole(Long roleId, MenuDTO dto, LoginUserDTO loginUserDTO);
}
