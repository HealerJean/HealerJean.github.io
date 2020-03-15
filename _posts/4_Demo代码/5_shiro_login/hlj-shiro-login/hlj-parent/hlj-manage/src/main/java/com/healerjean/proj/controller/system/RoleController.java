package com.healerjean.proj.controller.system;

import com.healerjean.proj.api.system.RoleService;
import com.healerjean.proj.controller.BaseController;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.dto.system.MenuDTO;
import com.healerjean.proj.dto.system.RoleDTO;
import com.healerjean.proj.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName RoleController
 * @Author TD
 * @Date 2019/3/18 14:05
 * @Description 角色管理controller
 */

@Api(description = "系统管理-角色管理")
@RestController
@RequestMapping("hlj/sys")
@Slf4j
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "角色添加",
            notes = "角色添加",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = RoleDTO.class)
    @PostMapping(value = "role/add")
    public ResponseBean addRoles(@RequestBody(required = false) RoleDTO roleDTO) {
        log.info("系统管理-角色管理--------角色添加--------请求参数：{}", roleDTO);
        roleService.addRole(roleDTO, UserUtils.getLoginUser());
        return ResponseBean.buildSuccess("角色角色添加成功", roleDTO);
    }


    @ApiOperation(value = "角色删除",
            notes = "角色删除",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = RoleDTO.class)
    @DeleteMapping(value = "role/{roleId}")
    public ResponseBean deleteRole(@PathVariable Long roleId) {
        log.info("系统管理-角色管理--------角色删除--------角色Id：{}", roleId);
        roleService.deleteRole(roleId, UserUtils.getLoginUser());
        return ResponseBean.buildSuccess("角色删除成功");
    }


    @ApiOperation(value = "角色修改",
            notes = "角色修改",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = RoleDTO.class)
    @PutMapping(value = "role/{roleId}")
    public ResponseBean updateRole(@PathVariable Long roleId, @RequestBody(required = false) RoleDTO roleDTO) {
        roleDTO.setId(roleId);
        log.info("系统管理-角色管理--------角色修改--------角色Id：{}，请求参数：{}", roleId, roleDTO);
        roleService.updateRole(roleDTO, UserUtils.getLoginUser());
        return ResponseBean.buildSuccess("角色修改成功");
    }


    @ApiOperation(value = "角色查询",
            notes = "角色查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = RoleDTO.class)
    @GetMapping(value = "role/{roleId}")
    public ResponseBean getRole(@PathVariable Long roleId) {
        log.info("系统管理-角色管理--------角色查询--------角色Id：{}", roleId);
        return ResponseBean.buildSuccess("角色查询成功", roleService.getRole(roleId));
    }


    @ApiOperation(value = "角色管理-角色列表查询",
            notes = "角色管理-角色列表查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = RoleDTO.class)
    @GetMapping(value = "roles")
    public ResponseBean getUsers(RoleDTO dto) {
        log.info("系统管理-角色管理--------角色列表查询--------请求参数：{}", dto);
        return ResponseBean.buildSuccess("角色列表查询成功", roleService.getRoles(dto));
    }


    @ApiOperation(value = "角色管理-角色权限修改",
            notes = "角色管理-角色权限修改",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = RoleDTO.class)
    @PutMapping(value = "role/{roleId}/menus")
    public ResponseBean updateMenusByRole(@PathVariable Long roleId, @RequestBody(required = false) MenuDTO menuDTO) {
        log.info("系统管理-角色管理--------角色权限修改--------角色Id：{}", roleId);
        roleService.updateMenusByRole(roleId, menuDTO, UserUtils.getLoginUser());
        return ResponseBean.buildSuccess("角色权限修改成功");
    }


    @ApiOperation(value = "角色管理-角色权限查询",
            notes = "角色管理-角色权限查询",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = RoleDTO.class)
    @GetMapping(value = "role/{roleId}/menus")
    public ResponseBean getMenusByRole(@PathVariable Long roleId, MenuDTO menuDTO) {
        log.info("系统管理-角色管理--------角色权限查询--------角色Id：{}", roleId);
        List<MenuDTO> menus = roleService.getMenusByRole(roleId, menuDTO);
        return ResponseBean.buildSuccess("角色列表查询成功", menus);
    }

}
