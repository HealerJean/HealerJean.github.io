/*
 * Powered By code-generator
 */
package com.healerjean.proj.service.system;

import com.healerjean.proj.api.system.MenuService;
import com.healerjean.proj.common.page.PageDTO;
import com.healerjean.proj.data.manager.system.SysMenuManager;
import com.healerjean.proj.data.pojo.system.SysMenu;
import com.healerjean.proj.data.pojo.system.SysMenuPage;
import com.healerjean.proj.data.pojo.system.SysMenuQuery;
import com.healerjean.proj.dto.system.MenuDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName MenuServiceImpl
 * @Author DYB
 * @Date 2019/6/11 9:42
 * @Description 菜单管理实现
 * @Version V1.0
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private SysMenuManager sysMenuManager;

    /**
     * 新增菜单
     *
     * @param menuDTO
     */
    @Override
    public void addMenu(MenuDTO menuDTO, LoginUserDTO loginUserDTO) {
        SysMenuQuery query = new SysMenuQuery();
        query.setMenuName(menuDTO.getMenuName());
        query.setRefSystemCode(menuDTO.getSystemCode());
        query.setPid(menuDTO.getPid());
        SysMenu result = sysMenuManager.findByQueryContion(query);
        if (result != null) {
            if (result.getStatus().equals(StatusEnum.生效.code)) {
                throw new BusinessException(ResponseEnum.菜单已经存在.code);
            } else {
                //存在已经被删除过的数据，还原为有效状态
                result.setUrl(menuDTO.getUrl());
                result.setDescription(menuDTO.getDescrption());
                result.setSort(menuDTO.getSort());
                result.setIcon(menuDTO.getIcon());
                result.setPchain(getPchain(result.getPid()));
                result.setStatus(StatusEnum.生效.code);
                result.setUpdateUser(loginUserDTO.getUserId());
                result.setUpdateName(loginUserDTO.getRealName());
                sysMenuManager.updateSelective(result);
            }
        } else {
            SysMenu menu = BeanUtils.dtoToMenu(menuDTO);
            menu.setPchain(getPchain(menu.getPid()));
            menu.setStatus(StatusEnum.生效.code);
            menu.setCreateUser(loginUserDTO.getUserId());
            menu.setCreateName(loginUserDTO.getRealName());
            sysMenuManager.insertSelective(menu);
        }
    }

    /**
     * 菜单列表查询，根据系统code，父级id，名称模糊查询
     *
     * @param dto
     * @return
     */
    @Override
    public PageDTO<MenuDTO> getMenus(MenuDTO dto) {
        SysMenuQuery query = new SysMenuQuery();
        query.setRefSystemCode(dto.getSystemCode());
        query.setMenuName(dto.getMenuName());
        query.setMenuType(dto.getMenuType());
        query.setPid(dto.getPid());
        if (dto.getPageNo() != null) {
            query.setPageNo(dto.getPageNo());
        }
        if (dto.getPageSize() != null) {
            query.setPageSize(dto.getPageSize());
        }
        query.setStatus(StatusEnum.生效.code);
        SysMenuPage page = sysMenuManager.queryPageListLike(query);
        List<SysMenu> menuList = page.getValues();
        List<MenuDTO> menuDTOS = new ArrayList<>();
        if (menuList != null) {
            menuList.forEach(item -> menuDTOS.add(BeanUtils.menuToDTO(item)));
        }
        return BeanUtils.toPageDTO(page, menuDTOS);
    }


    /**
     * 菜单列表查询无分页,返回树状
     *
     * @param menuDTO
     * @return
     */
    @Override
    public Map<String, MenuDTO> getMenusNoPageToTree (MenuDTO menuDTO) {
        SysMenuQuery query = new SysMenuQuery();
        query.setRefSystemCode(menuDTO.getSystemCode());
        query.setStatus(StatusEnum.生效.code);
        List<SysMenu> menus = sysMenuManager.queryList(query);
        if(menus == null){
            return new HashMap<>(0);
        }
        Map<String, List<SysMenu>> mapSystem = menus.stream().collect(Collectors.groupingBy(SysMenu::getRefSystemCode));
        Map<String, MenuDTO> result = new HashMap<>(mapSystem.size());
        //前后分端菜单按菜单类型做分组
        for(Map.Entry<String, List<SysMenu>> systemEntry : mapSystem.entrySet()) {
            MenuDTO resultMenuDTO =  new MenuDTO();
            List<SysMenu> systemValue = systemEntry.getValue();
            if(systemValue == null){
                continue;
            }
            Map<String, List<SysMenu>> mapMenu = systemValue.stream().collect(Collectors.groupingBy(item -> item.getMenuType()));
            //分开后做树状菜单
            //前端菜单树状
            List<MenuDTO> data = null;
            List<SysMenu> displayMenus = mapMenu.get(BusinessEnum.MenuTypeEnum.前端菜单.code);
            if (displayMenus != null) {
                data = displayMenus.stream().map(item -> BeanUtils.menuToDTO(item)).collect(Collectors.toList());
                data = BeanUtils.recursionMenus(data, 0L);
                resultMenuDTO.setFrontMenus(data);
            }
            //后端菜单树状
            List<SysMenu> serviceMenus = mapMenu.get(BusinessEnum.MenuTypeEnum.后端菜单.code);
            if(serviceMenus != null) {
                List<MenuDTO> serviceData = serviceMenus.stream().map(item -> BeanUtils.menuToDTO(item)).collect(Collectors.toList());
                List<MenuDTO> systemData = systemValue.stream().map(item -> BeanUtils.menuToDTO(item)).collect(Collectors.toList());
                serviceData = BeanUtils.backMenusToTree(serviceData, systemData);
                resultMenuDTO.setBackMenus(serviceData);
            }
            result.put(systemEntry.getKey(),resultMenuDTO);
        }
        return result;
    }


    /**
     * 菜单查询
     */
    @Override
    public MenuDTO getMenu(Integer menuId) {
        SysMenu result = sysMenuManager.findById(menuId);
        if (result != null) {
            return BeanUtils.menuToDTO(result);
        } else {
            throw new BusinessException(ResponseEnum.菜单不存在.code);
        }
    }

    /**
     * 菜单修改
     *
     * @param menuDTO
     */
    @Override
    public void updateMenu(MenuDTO menuDTO, LoginUserDTO loginUserDTO) {
        SysMenuQuery query = new SysMenuQuery();
        query.setMenuName(menuDTO.getMenuName());
        query.setRefSystemCode(menuDTO.getSystemCode());
        query.setPid(menuDTO.getPid());
        SysMenu result = sysMenuManager.findByQueryContion(query);
        if (result != null && !result.getId().equals(menuDTO.getId())) {
            if (result.getStatus().equals(StatusEnum.生效.code)) {
                throw new BusinessException(ResponseEnum.菜单已经存在.code);
            } else {
                //存在已经被删除过的数据，彻底删除
                sysMenuManager.deleteById(result.getId());
            }
        }
        SysMenu menu = BeanUtils.dtoToMenu(menuDTO);
        menu.setPchain(getPchain(menuDTO.getPid()));
        menu.setUpdateUser(loginUserDTO.getUserId());
        menu.setUpdateName(loginUserDTO.getRealName());
        int i = sysMenuManager.updateSelective(menu);
        if (i < 1) {
            throw new BusinessException(ResponseEnum.菜单不存在.code);
        }
    }

    /**
     * 菜单删除
     *
     * @param menuIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(List<Long> menuIds) {
        List<Long> collect = menuIds.stream().distinct().collect(Collectors.toList());
        List<SysMenu> menus = sysMenuManager.findByIds(menuIds);
        if (menus == null || menus.size() != collect.size()) {
            throw new BusinessException(ResponseEnum.菜单不存在.code);
        }
        //逻辑删除
        menus.forEach(item -> {
            item.setStatus(StatusEnum.废弃.code);
        });
        sysMenuManager.batchUpdate(menus);
    }

    /**
     * 获取父菜单链，格式：2,1,0
     *
     * @param pid
     * @return
     */
    private String getPchain(Long pid) {
        Long id = pid;
        StringBuffer stringBuffer = new StringBuffer(id + ",");
        while (id != 0) {
            SysMenu menu = sysMenuManager.findById(id);
            id = menu.getPid();
            stringBuffer.append(id).append(",");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }
}
