/*
 * Powered By code-generator
 */
package com.healerjean.proj.api.system;

import com.healerjean.proj.common.page.PageDTO;
import com.healerjean.proj.dto.system.MenuDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;

import java.util.List;
import java.util.Map;

/**
 * @ClassName MenuService
 * @Author DYB
 * @Date 2019/6/11 9:42
 * @Description 菜单管理接口
 * @Version V1.0
 */
public interface MenuService {

    /**
     * 菜单添加
     */
    void addMenu(MenuDTO menu, LoginUserDTO loginUserDTO);

    /**
     * 菜单列表查询
     */
    PageDTO<MenuDTO> getMenus(MenuDTO query);

    /**
     * 菜单列表查询无分页,返回树状
     */
    Map<String, MenuDTO> getMenusNoPageToTree(MenuDTO menuDTO);

    /**
     * 菜单查询
     */
    MenuDTO getMenu(Integer menuId);

    /**
     * 菜单修改
     */
    void updateMenu(MenuDTO menuDTO, LoginUserDTO loginUserDTO);

    /**
     * 菜单删除
     */
    void deleteMenu(List<Long> menuIds);
}
