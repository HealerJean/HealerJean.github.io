/*
 * Powered By code-generator
 */
package com.healerjean.proj.service.system;

import com.healerjean.proj.api.system.RoleService;
import com.healerjean.proj.common.page.PageDTO;
import com.healerjean.proj.data.manager.system.SysMenuManager;
import com.healerjean.proj.data.manager.system.SysRoleManager;
import com.healerjean.proj.data.manager.system.SysRoleMenuRefManager;
import com.healerjean.proj.data.pojo.system.*;
import com.healerjean.proj.dto.system.MenuDTO;
import com.healerjean.proj.dto.system.RoleDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.enums.BusinessEnum;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleManager roleManager;

    @Autowired
    private SysMenuManager menuManager;

    @Autowired
    private SysRoleMenuRefManager roleMenuRefManager;

    /**
     * 角色添加
     *
     * @param roleDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleDTO roleDTO, LoginUserDTO loginUserDTO) {
        SysRole role = BeanUtils.DTOtoRole(roleDTO);
        SysRoleQuery query = new SysRoleQuery();
        query.setRoleName(role.getRoleName());
        query.setRefSystemCode(role.getRefSystemCode());
        query.setStatus(StatusEnum.生效.code);
        List<SysRole> resultList = roleManager.queryList(query);
        if (resultList != null && !resultList.isEmpty()) {
            throw new BusinessException(ResponseEnum.角色已经存在.code);
        }
        role.setStatus(StatusEnum.生效.code);
        role.setCreateUser(loginUserDTO.getUserId());
        role.setCreateName(loginUserDTO.getRealName());
        roleManager.insertSelective(role);
        roleDTO.setId(role.getId());
    }

    /**
     * 角色列表查询，根据系统CODE，名称模糊查询
     *
     * @param dto
     * @return
     */
    @Override
    public PageDTO<RoleDTO> getRoles(RoleDTO dto) {
        SysRolePage page = null;
        SysRoleQuery query = new SysRoleQuery();
        List<SysRole> roles;
        if (dto.getIsPage() == null || !dto.getIsPage()) {
            //查询不分页
            query.setStatus(StatusEnum.生效.code);
            roles = roleManager.queryListLike(query);
        } else {
            if (dto != null) {
                query.setRefSystemCode(dto.getSystemCode());
                if (dto.getPageNo() != null) {
                    query.setPageNo(dto.getPageNo());
                }
                if (dto.getPageSize() != null) {
                    query.setPageSize(dto.getPageSize());
                }
                query.setRefSystemCode(dto.getSystemCode());
                query.setRoleName(dto.getRoleName());
            }
            query.setStatus(StatusEnum.生效.code);
            page = roleManager.queryPageListLike(query);
            roles = page.getValues();
        }
        List<RoleDTO> roleDTOS = new ArrayList<>();
        if (roles != null) {
            roles.forEach(item -> roleDTOS.add(BeanUtils.roleToDTO(item)));
        }
        return BeanUtils.toPageDTO(page, roleDTOS);
    }

    @Override
    public List<RoleDTO> getRolesNoPage(RoleDTO roleDTO) {
        SysRoleQuery query = new SysRoleQuery();
        query.setRefSystemCode(roleDTO.getSystemCode());
        query.setRoleName(roleDTO.getRoleName());
        query.setStatus(StatusEnum.生效.code);
        List<SysRole> roles = roleManager.queryListLike(query);
        if (roles != null) {
            return roles.stream().map(BeanUtils::roleToDTO).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 角色查询
     *
     * @param roleId
     * @return
     */
    @Override
    public RoleDTO getRole(Long roleId) {
        SysRole result = roleManager.findById(roleId);
        if (result != null) {
            RoleDTO roleDTO = BeanUtils.roleToDTO(result);
            //查询菜单
            List<MenuDTO> menusByRole = this.getMenusByRole(roleId, new MenuDTO());
            //前后分端菜单按菜单类型做分组
            Map<String, List<MenuDTO>> collect = menusByRole.stream().collect(Collectors.groupingBy(item -> item.getMenuType()));
            //分开后做树状菜单
            for (Map.Entry<String, List<MenuDTO>> entry : collect.entrySet()) {
                String key = entry.getKey();
                List<MenuDTO> value = entry.getValue();
                if (BusinessEnum.MenuTypeEnum.后端菜单.code.equals(key)) {
                    value = menusByRole;
                }
                value = BeanUtils.recursionMenus(value, 0L);
                switch (BusinessEnum.MenuTypeEnum.toEnum(key)) {
                    case 后端菜单:
                        roleDTO.setBackMenus(value);
                        break;
                    case 前端菜单:
                        roleDTO.setFrontMenus(value);
                        break;
                    default:
                }
            }
            return roleDTO;

        } else {
            throw new BusinessException(ResponseEnum.角色不存在.code);
        }
    }


    /**
     * 角色修改
     *
     * @param roleDTO
     */
    @Override
    public void updateRole(RoleDTO roleDTO, LoginUserDTO loginUserDTO) {
        SysRoleQuery query = new SysRoleQuery();
        query.setRoleName(roleDTO.getRoleName());
        query.setRefSystemCode(roleDTO.getSystemCode());
        query.setStatus(StatusEnum.生效.code);
        List<SysRole> resultList = roleManager.queryList(query);
        if (resultList.size() > 1 || (resultList.size() == 1 && !resultList.get(0).getId().equals(roleDTO.getId()))) {
            throw new BusinessException(ResponseEnum.角色已经存在.code);
        }
        SysRole role = BeanUtils.DTOtoRole(roleDTO);
        role.setUpdateUser(loginUserDTO.getUserId());
        role.setUpdateName(loginUserDTO.getRealName());
        int i = roleManager.updateSelective(role);
        if (i < 1) {
            throw new BusinessException(ResponseEnum.角色不存在.code);
        }
    }

    /**
     * 角色删除
     *
     * @param roleId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId, LoginUserDTO loginUserDTO) {
        SysRole role = roleManager.findById(roleId);
        if (role == null) {
            throw new BusinessException(ResponseEnum.角色不存在.code);
        }
        role.setStatus(StatusEnum.废弃.code);
        role.setUpdateUser(loginUserDTO.getUserId());
        role.setUpdateName(loginUserDTO.getRealName());
        roleManager.updateSelective(role);
        SysRoleMenuRefQuery query = new SysRoleMenuRefQuery();
        query.setRefRoleId(roleId);
        roleMenuRefManager.delete(query);
    }

    /**
     * 角色权限查询
     *
     * @param roleId
     * @return
     */
    @Override
    public List<MenuDTO> getMenusByRole(Long roleId, MenuDTO dto) {
        SysMenuQuery menuQuery = new SysMenuQuery();
        menuQuery.setStatus(StatusEnum.生效.code);
        menuQuery.setMenuName(dto.getMenuName());
        menuQuery.setMenuType(dto.getMenuType());
        menuQuery.setRefRoleId(roleId);
        List<SysMenu> menus = menuManager.selectMenusByRoleId(menuQuery);
        if (menus != null) {
            return menus.stream().map(BeanUtils::menuToDTO).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 角色权限修改
     *
     * @param roleId
     * @param menuDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenusByRole(Long roleId, MenuDTO menuDTO, LoginUserDTO loginUserDTO) {
        //先将角色现有菜单删除，再批量添加菜单信息
        SysRoleMenuRefQuery delQuery = new SysRoleMenuRefQuery();
        delQuery.setRefRoleId(roleId);
        roleMenuRefManager.delete(delQuery);
        List<Long> back = menuDTO.getBack();
        List<Long> front = menuDTO.getFront();
        List<SysRoleMenuRef> refRoleMenus = new ArrayList<>();
        //添加前端菜单
        if (front != null) {
            for (Long i : front) {
                refRoleMenus.add(toRefRoleMenu(roleId, loginUserDTO, i));
            }
        }
        //添加后端菜单
        if (back != null) {
            List<SysMenu> ids = menuManager.findByIds(back);
            List<Long> longList = ids.stream().filter(i -> BusinessEnum.MenuTypeEnum.后端菜单.code.equals(i.getMenuType()))
                    .map(SysMenu::getId).collect(Collectors.toList());
            for (Long i : longList) {
                refRoleMenus.add(toRefRoleMenu(roleId, loginUserDTO, i));
            }
        }
        roleMenuRefManager.batchInsert(refRoleMenus);
    }

    private SysRoleMenuRef toRefRoleMenu(Long roleId, LoginUserDTO loginUserDTO, Long menuId) {
        SysRoleMenuRef refMenu = new SysRoleMenuRef();
        refMenu.setRefRoleId(roleId);
        refMenu.setRefMenuId(menuId);
        refMenu.setStatus(StatusEnum.生效.code);
        refMenu.setCreateUser(loginUserDTO.getUserId());
        refMenu.setCreateName(loginUserDTO.getRealName());
        return refMenu;
    }
}
