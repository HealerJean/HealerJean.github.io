package com.healerjean.proj.service.identity.impl;

import com.healerjean.proj.data.manager.system.SysMenuManager;
import com.healerjean.proj.data.manager.system.SysUserDepartmentRefManager;
import com.healerjean.proj.data.manager.system.SysUserInfoManager;
import com.healerjean.proj.data.manager.system.SysUserRoleRefManager;
import com.healerjean.proj.data.pojo.system.*;
import com.healerjean.proj.dto.system.DepartmentDTO;
import com.healerjean.proj.dto.system.RoleDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.enums.BusinessEnum;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.service.identity.IdentityService;
import com.healerjean.proj.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName 用户认证信息接口实现
 * @Date 2019/10/18  17:06.
 * @Description
 */
@Service
public class IdentityServiceImpl implements IdentityService {

    @Autowired
    private SysUserInfoManager sysUserInfoManager;
    @Autowired
    private SysUserRoleRefManager sysUserRoleRefManager;
    @Autowired
    private SysMenuManager sysMenuManager;
    @Autowired
    private SysUserDepartmentRefManager sysUserUserDepartmentRefManager;


    /**
     * 根据用户ID获取当前登陆用户信息
     * 1、用户基本信息
     * 2、用户所处部门信息
     * 3、角色集合
     * 4、获取角色所对应的所有菜单（这里的菜单指的是所有的url，包括前台菜单）
     */
    @Override
    public LoginUserDTO getUserInfo(Long userId) {
        if (userId == null || userId < 0) {
            throw new BusinessException("用户ID不能为空");
        }
        //1、用户基本信息
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        SysUserInfo userInfo = sysUserInfoManager.findById(userId);
        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }
        loginUserDTO.setUserId(userInfo.getId());
        loginUserDTO.setUserName(userInfo.getUserName());
        loginUserDTO.setRealName(userInfo.getRealName());
        loginUserDTO.setEmail(userInfo.getEmail());
        loginUserDTO.setTelephone(userInfo.getTelephone());
        loginUserDTO.setUserType(userInfo.getUserType());

        //用户所处部门信息
        SysUserDepartmentRefQuery sysUserDepartmentRefQuery = new SysUserDepartmentRefQuery();
        sysUserDepartmentRefQuery.setRefUserId(userInfo.getId());
        sysUserDepartmentRefQuery.setStatus(StatusEnum.生效.code);
        SysDepartment sysDepartment = sysUserUserDepartmentRefManager.findByQueryContionToDepartment(sysUserDepartmentRefQuery);
        DepartmentDTO departmentDTO = BeanUtils.toDepartmentDTO(sysDepartment);
        loginUserDTO.setDepartmentDTO(departmentDTO);


        // 2、角色集合
        SysUserRoleRefQuery userRoleRefQuery = new SysUserRoleRefQuery();
        userRoleRefQuery.setRefUserId(userId);
        List<SysRole> roles = sysUserRoleRefManager.queryListToRole(userRoleRefQuery);
        if (roles != null && !roles.isEmpty()) {
            List<RoleDTO> roleDTOS = new ArrayList<>();
            for (SysRole u : roles) {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setId(u.getId());
                roleDTO.setRoleName(u.getRoleName());
                roleDTOS.add(roleDTO);
            }
            loginUserDTO.setRoles(roleDTOS);
            //  3、获取所有角色所对应的所有菜单（这里的菜单指的是所有的url，包括前台菜单）
            SysMenuQuery sysMenuQuery = new SysMenuQuery();
            sysMenuQuery.setRefRoleIds(roles.stream().map(item -> item.getId()).collect(Collectors.toList()));
            List<SysMenu> menuList = sysMenuManager.queryListToMenu(sysMenuQuery);

            List<SysMenu> menus = new ArrayList<>();
            List<SysMenu> permissions = new ArrayList<>();
            for (SysMenu menu : menuList) {
                //所有有效的菜单
                if (StatusEnum.生效.code.equals(menu.getIsPermission())) {
                    permissions.add(menu);
                }
                //前端菜单
                if (BusinessEnum.MenuTypeEnum.前端菜单.code.equals(menu.getMenuType())) {
                    menus.add(menu);
                }
            }
            //递归获取树形关系
            loginUserDTO.setMenus(BeanUtils.menuListToDTOsTree(menus));

            //讲权限集合转化为DTO集合
            loginUserDTO.setPermissions(BeanUtils.menuListToDTOs(permissions));
        }
        return loginUserDTO;
    }
}
