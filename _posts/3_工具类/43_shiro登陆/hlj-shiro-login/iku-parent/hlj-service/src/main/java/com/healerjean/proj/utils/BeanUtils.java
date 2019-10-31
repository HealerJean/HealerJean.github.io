package com.healerjean.proj.utils;

import com.healerjean.proj.common.page.PageDTO;
import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.common.result.PageListResult;
import com.healerjean.proj.data.pojo.system.*;
import com.healerjean.proj.dto.system.*;
import com.healerjean.proj.dto.user.UserDTO;
import com.healerjean.proj.enums.StatusEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName BeanUtils
 * @date 2019/6/13  20:08.
 * @Description
 */
public class BeanUtils {


    public static <T> PageDTO<T> toPageDTO(PageListResult pageView, List<T> datas) {
        if (pageView == null && (datas == null || datas.isEmpty())) {
            return null;
        } else if (pageView == null) {
            return new PageDTO<>(datas);
        } else if (pageView.getPagenation() == null) {
            return new PageDTO<>(datas);
        } else {
            Pagenation pagenation = pageView.getPagenation();
            return new PageDTO(pagenation.getPageNo(), pagenation.getPageSize(), pagenation.getItemCount(),
                    pagenation.getPageCount(), datas);
        }
    }




    public static List<MenuDTO> menuListToDTOs(List<SysMenu> menuList) {
        List<MenuDTO> menuDTOs = new ArrayList<>();
        for (SysMenu menu : menuList) {
            MenuDTO dto = new MenuDTO();
            dto.setMenuName(menu.getMenuName());
            dto.setUrl(menu.getUrl());
            dto.setMethod(menu.getMethod());
            if (menu.getSort() != null) {
                dto.setSort(menu.getSort());
            }
            dto.setIcon(menu.getIcon());
            dto.setFrontKey(menu.getFrontKey());
            dto.setMenuType(menu.getMenuType());
            menuDTOs.add(dto);
        }
        return menuDTOs;
    }


    /**
     * 获取前端_路由菜单树形集合
     *
     * @param menuList
     * @return
     */
    public static List<MenuDTO> menuListToDTOsTree(List<SysMenu> menuList) {
        return menuRecursion(menuList, 0L);
    }

    private static List<MenuDTO> menuRecursion(List<SysMenu> menuList, Long pid) {
        List<MenuDTO> menuDTOs = new ArrayList<>();
        for (SysMenu u : menuList) {
            if (u.getPid().equals(pid)) {
                MenuDTO menuDTO = new MenuDTO();
                menuDTO.setMenuName(u.getMenuName());
                menuDTO.setUrl(u.getUrl());
                menuDTO.setMethod(u.getMethod());
                menuDTO.setIcon(u.getIcon());
                if (u.getSort() != null) {
                    menuDTO.setSort(u.getSort());
                }
                menuDTO.setFrontKey(u.getFrontKey());
                menuDTO.setMenuType(u.getMenuType());
                menuDTO.setSubmenus(menuRecursion(menuList, u.getId()));
                menuDTOs.add(menuDTO);
            }
        }
        //根据sort排序
        menuDTOs.sort((o1, o2) -> {
            if (o1.getSort() == null) {
                return 1;
            }
            if (o2.getSort() == null) {
                return -1;
            }
            if (Integer.valueOf(o1.getSort()).equals(Integer.valueOf(o2.getSort()))) {
                return 0;
            } else if (Integer.valueOf(o1.getSort()) < Integer.valueOf(o2.getSort())) {
                return -1;
            } else {
                return 1;
            }
        });
        return menuDTOs;
    }

    /**
     * 递归菜单
     *
     * @param menus
     * @return
     */
    public static List<MenuDTO> recursionMenus(List<MenuDTO> menus, long pid) {
        List<MenuDTO> result = new LinkedList<>();
        if (menus != null) {
            for (MenuDTO menuDTO : menus) {
                if (menuDTO.getPid().equals(pid)) {
                    MenuDTO menuDTO1 = new MenuDTO();
                    menuDTO1.setId(menuDTO.getId());
                    menuDTO1.setMenuName(menuDTO.getMenuName());
                    menuDTO1.setSystemCode(menuDTO.getSystemCode());
                    menuDTO1.setUrl(menuDTO.getUrl());
                    menuDTO1.setSort(menuDTO.getSort());
                    menuDTO1.setMethod(menuDTO.getMethod());
                    menuDTO1.setStatus(menuDTO.getStatus());
                    menuDTO1.setDescrption(menuDTO.getDescrption());
                    menuDTO1.setPid(menuDTO.getPid());
                    menuDTO1.setPchain(menuDTO.getPchain());
                    menuDTO1.setSort(menuDTO.getSort());
                    menuDTO1.setIcon(menuDTO.getIcon());
                    menuDTO1.setFrontKey(menuDTO.getFrontKey());
                    menuDTO1.setIsPermission(menuDTO.getIsPermission());
                    menuDTO1.setMenuType(menuDTO.getMenuType());
                    menuDTO1.setSubmenus(recursionMenus(menus, menuDTO.getId()));
                    result.add(menuDTO1);
                }
            }
        }
        result.sort((o1, o2) -> {
            if (o1.getSort() == null && o2.getSort() == null) {
                return 0;
            }
            if (o1.getSort() == null) {
                return 1;
            }
            if (o2.getSort() == null) {
                return -1;
            }
            if (Integer.valueOf(o1.getSort()).equals(Integer.valueOf(o2.getSort()))) {
                return 0;
            } else if (Integer.valueOf(o1.getSort()) < Integer.valueOf(o2.getSort())) {
                return -1;
            }
            return 1;

        });
        return result;
    }

    /**
     * 后端菜单树状形成,定制化
     *
     * @param backMenus
     * @param allMenus
     * @return
     */
    public static List<MenuDTO> backMenusToTree(List<MenuDTO> backMenus, List<MenuDTO> allMenus) {
        if (backMenus == null || backMenus.isEmpty()) {
            return new ArrayList<>();
        }
        if (allMenus == null || allMenus.isEmpty()) {
            return backMenus;
        }
        //首先获取出所有后端菜单的父菜单
        Set<Long> set = new HashSet<>();
        for (MenuDTO backMenu : backMenus) {
            String pchain = backMenu.getPchain();
            if (StringUtils.isBlank(pchain)) {
                continue;
            }
            String[] split = pchain.split(",");
            List<Long> collect = Arrays.stream(split).map(Long::valueOf).collect(Collectors.toList());
            set.addAll(collect);
            set.add(backMenu.getId());
        }
        //排序递归
        List<Long> collect = set.stream().sorted().collect(Collectors.toList());
        Map<Long, MenuDTO> map = new HashMap<>(collect.size() >> 2);
        map.put(0L, new MenuDTO());
        for (MenuDTO menus : allMenus) {
            Long id = menus.getId();
            if (collect.contains(id)) {
                map.put(id, menus);
            }
        }
        //构建一颗树结构
        for (Long i : collect) {
            if (i != 0) {
                MenuDTO menuDTO = map.get(i);
                if (menuDTO == null) {
                    continue;
                }
                Long pid = menuDTO.getPid();
                MenuDTO pMenu = map.get(pid);
                List<MenuDTO> children = pMenu.getSubmenus();
                if (children == null) {
                    children = new LinkedList<>();
                    pMenu.setSubmenus(children);
                }
                children.add(menuDTO);
            }
        }
        return map.get(collect.get(0)).getSubmenus();
    }

    public static UserDTO userInfoToDTO(SysUserInfo userInfo) {
        UserDTO dto = new UserDTO();
        dto.setUserName(userInfo.getUserName());
        dto.setEmail(userInfo.getEmail());
        dto.setUserType(userInfo.getUserType());
        dto.setTelephone(userInfo.getTelephone());
        dto.setRealName(userInfo.getRealName());
        dto.setPassword(userInfo.getPassword());
        dto.setUserId(userInfo.getId());
        dto.setSalt(userInfo.getSalt());
        return dto;
    }


    /**
     * 菜单pojo转dto
     */
    public static MenuDTO menuToDTO(SysMenu menu) {
        MenuDTO dto = new MenuDTO();
        dto.setId(menu.getId());
        dto.setMenuName(menu.getMenuName());
        dto.setSystemCode(menu.getRefSystemCode());
        dto.setUrl(menu.getUrl());
        dto.setMethod(menu.getMethod());
        dto.setStatus(menu.getStatus());
        dto.setPid(menu.getPid());
        dto.setPchain(menu.getPchain());
        dto.setDescrption(menu.getDescription());
        dto.setSort(menu.getSort());
        dto.setIcon(menu.getIcon());
        dto.setFrontKey(menu.getFrontKey());
        dto.setIsPermission(menu.getIsPermission());
        dto.setMenuType(menu.getMenuType());
        return dto;
    }

    /**
     * 菜单dto转pojo
     */
    public static SysMenu dtoToMenu(MenuDTO dto) {
        SysMenu menu = new SysMenu();
        menu.setId(dto.getId());
        menu.setMenuName(dto.getMenuName());
        menu.setRefSystemCode(dto.getSystemCode());
        menu.setUrl(dto.getUrl());
        menu.setMethod(dto.getMethod());
        menu.setStatus(dto.getStatus());
        menu.setPid(dto.getPid());
        menu.setPchain(dto.getPchain());
        menu.setDescription(dto.getDescrption());
        menu.setSort(dto.getSort());
        menu.setIcon(dto.getIcon());
        menu.setFrontKey(dto.getFrontKey());
        menu.setIsPermission(dto.getIsPermission());
        menu.setMenuType(dto.getMenuType());
        return menu;
    }

    /**
     * 角色pojo转dto
     */
    public static RoleDTO roleToDTO(SysRole role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        dto.setSystemCode(role.getRefSystemCode());
        dto.setStatus(role.getStatus());
        dto.setDescription(role.getDesciption());
        return dto;
    }

    /**
     * 角色dto转pojo
     */
    public static SysRole DTOtoRole(RoleDTO dto) {
        SysRole role = new SysRole();
        role.setId(dto.getId());
        role.setRoleName(dto.getRoleName());
        role.setRefSystemCode(dto.getSystemCode());
        role.setStatus(dto.getStatus());
        role.setDesciption(dto.getDescription());
        return role;
    }


    public static DictionaryDataDTO dictionaryDataToDTO(SysDictionaryData data) {
        DictionaryDataDTO result = new DictionaryDataDTO();
        if (data != null) {
            result.setId(data.getId());
            result.setTypeKey(data.getRefTypeKey());
            result.setDataKey(data.getDataKey());
            result.setDataValue(data.getDataValue());
            result.setStatus(data.getStatus());
            result.setPageNo(null);
            result.setPageSize(null);
            result.setFlag(null);
            result.setTypeKeys(null);
        }
        return result;
    }

    public static DictionaryTypeDTO dictionaryTypeToDTO(SysDictionaryType type) {
        DictionaryTypeDTO result = new DictionaryTypeDTO();
        if (type != null) {
            result.setId(type.getId());
            result.setTypeKey(type.getTypeKey());
            result.setTypeDesc(type.getTypeDesc());
            result.setStatus(type.getStatus());
            result.setPageNo(null);
            result.setPageSize(null);
        }
        return result;
    }
    public static SysDictionaryType dtoToDictionaryType(DictionaryTypeDTO typeDTO) {
        SysDictionaryType result = new SysDictionaryType();
        result.setId(typeDTO.getId());
        result.setTypeKey(typeDTO.getTypeKey());
        result.setTypeDesc(typeDTO.getTypeDesc());
        result.setStatus(typeDTO.getStatus());
        return result;
    }

    public static DistrictDTO toDistrictDTO(SysDistrict sysDistrict) {
        DistrictDTO districtDTO = new DistrictDTO();
        districtDTO.setProvinceCode(sysDistrict.getProvinceCode());
        districtDTO.setProvinceName(sysDistrict.getProvinceName());
        districtDTO.setCityCode(sysDistrict.getCityCode());
        districtDTO.setCityName(sysDistrict.getCityName());
        districtDTO.setDistrictCode(sysDistrict.getDistrictCode());
        districtDTO.setDistrictName(sysDistrict.getDistrictName());
        return districtDTO;
    }

    public static ProvinceDTO toProvinceDTO(SysDistrict sysDistrict) {
        ProvinceDTO provinceDTO = new ProvinceDTO();
        provinceDTO.setProvinceCode(sysDistrict.getProvinceCode());
        provinceDTO.setProvinceName(sysDistrict.getProvinceName());
        return provinceDTO;
    }

    public static CityDTO toCityDTO(SysDistrict sysDistrict) {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setProvinceCode(sysDistrict.getProvinceCode());
        cityDTO.setProvinceName(sysDistrict.getProvinceName());
        cityDTO.setCityCode(sysDistrict.getCityCode());
        cityDTO.setCityName(sysDistrict.getCityName());
        return cityDTO;
    }

    public static void main(String[] args) {
        // CodeAutoUtils.beanCopy(DomainDTO.class, SysDomain.class, "dto", "domain");
        // CodeAutoUtils.beanCopy(ItemGood.class, ItemGoodDTO.class, "itemGood", "dto");
    }


    /**
     * 部门集合转换树形结构
     *
     * @param departmentList
     * @return
     */
    public static List<DepartmentDTO> departmentListToDTOsTree(List<SysDepartment> departmentList) {
        return departmentRecursion(departmentList, 0L);
    }

    private static List<DepartmentDTO> departmentRecursion(List<SysDepartment> departmentList, Long pid) {
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();
        for (SysDepartment d : departmentList) {
            if (d.getPid().equals(pid)) {
                DepartmentDTO departmentDTO = new DepartmentDTO();
                departmentDTO.setId(d.getId());
                departmentDTO.setDepartmentName(d.getName());
                departmentDTO.setDepartmentDesc(d.getDescription());
                departmentDTO.setPid(d.getPid());
                departmentDTO.setStatus(d.getStatus());
                departmentDTO.setChildDepartment(departmentRecursion(departmentList, d.getId()));
                departmentDTOS.add(departmentDTO);
            }
        }
        return departmentDTOS;
    }

    /**
     * 部门dto转pojo
     *
     * @param dto
     * @return
     */
    public static SysDepartment departmentDtoToPojo(DepartmentDTO dto) {
        SysDepartment department = new SysDepartment();
        department.setId(dto.getId());
        department.setName(dto.getDepartmentName());
        department.setDescription(dto.getDepartmentDesc());
        if (dto.getPid() == null) {
            department.setPid(0L);
        } else {
            department.setPid(dto.getPid());
        }
        department.setStatus(StatusEnum.生效.code);
        return department;
    }


    public static DepartmentDTO toDepartmentDTO(SysDepartment sysDepartment) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        if(sysDepartment != null){
            departmentDTO.setId(sysDepartment.getId());
            departmentDTO.setDepartmentName(sysDepartment.getName());
            departmentDTO.setDepartmentDesc(sysDepartment.getDescription());
        }
        return departmentDTO;
    }

}
